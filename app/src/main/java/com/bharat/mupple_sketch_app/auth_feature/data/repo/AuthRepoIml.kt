package com.bharat.mupple_sketch_app.auth_feature.data.repo

import androidx.compose.ui.geometry.Rect
import com.bharat.mupple_sketch_app.app_root.TriggerListenerFlag
import com.bharat.mupple_sketch_app.auth_feature.domain.repo.AuthRepo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoIml @Inject constructor(
    private val authInstance : FirebaseAuth,
    private val firestoreInstance : FirebaseFirestore,
)  : AuthRepo {
    override fun loginWithGoogle(cred: AuthCredential, email : String): Flow<Result<Unit>> {
        return flow {
            val query = firestoreInstance.collection("users").whereEqualTo("email", email).limit(1).get().await()
            if(!query.isEmpty){
                authInstance.signInWithCredential(cred).await()
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("User does not exists.")))
            }
        }.catch {
            emit(Result.failure(it))
        }
    }


    override fun registerWithGoogle(cred: AuthCredential, email: String): Flow<Result<Unit>> {
       return flow {
           val query = firestoreInstance.collection("users").whereEqualTo("email", email).limit(1).get().await()

           if(query.isEmpty){
               authInstance.signInWithCredential(cred).await()
               emit(Result.success(Unit))
           } else {
               emit(Result.failure(Exception("User alredy exists.")))
           }

       }.catch { e ->
           emit(Result.failure(Exception(e.message)))
       }
    }

    override fun saveUserDetails(): Flow<Result<Unit>> {
        return flow {
            val emailFeild = mapOf("email" to authInstance.currentUser?.email!!)
            firestoreInstance.collection("users").document(authInstance.currentUser?.uid!!).set(emailFeild).await()
            emit(Result.success(Unit))
        } .catch { e ->
            emit(Result.failure(Exception(e.message)))
        }
    }

    override fun signOut() {
        authInstance.signOut()
    }

}