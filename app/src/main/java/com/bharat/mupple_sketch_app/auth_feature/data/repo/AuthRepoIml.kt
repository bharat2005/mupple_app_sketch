package com.bharat.mupple_sketch_app.auth_feature.data.repo

import androidx.compose.ui.geometry.Rect
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
    private val firestoreInstance : FirebaseFirestore
)  : AuthRepo {
    override fun loginWithGoogle(cred: AuthCredential, email : String): Flow<Result<Unit>> {
        return flow {
            val query = firestoreInstance.collection("users").whereEqualTo("email", email).limit(1).get().await()
            if(!query.isEmpty){
                emit(Result.success(Unit))
                authInstance.signInWithCredential(cred).await()
            } else {
                emit(Result.failure(Exception("User does not exists.")))
            }
        }.catch {
            emit(Result.failure(it))
        }
    }
}