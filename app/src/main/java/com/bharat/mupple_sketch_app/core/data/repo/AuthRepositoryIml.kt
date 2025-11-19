package com.bharat.mupple_sketch_app.core.data.repo

import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthEvents{
    object Success : AuthEvents()
    data class Error(val message : String) : AuthEvents()
}


enum class AuthState{
    UNKNOWN,
    UNAUTHENTICATED,
    PERSONALALIZATION_INCOMPLETE,
    AUTHENTICATED
}


@Singleton
class AuthRepositoryIml @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository{

    private val _authEvents = MutableSharedFlow<AuthEvents>()
    override fun getAuthEvents(): Flow<AuthEvents> = _authEvents.asSharedFlow()
    private val _triggerListenerState = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAuthState(): Flow<AuthState> {
        val firebaseAuthFlow = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener{ firebaseAuth ->
                trySend(firebaseAuth.currentUser)
            }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }
        return combine(
            firebaseAuthFlow,
            _triggerListenerState
        ){ user,_ -> user}
            .flatMapLatest { user ->
                if(user == null){
                    flowOf(AuthState.UNAUTHENTICATED)
                } else {
                    try {
                        val userDoc = firestore.collection("users").document(user.uid).get(Source.SERVER).await()
                        if(userDoc.exists()){
                            val hasCompleted = userDoc.getBoolean("hasCompletedPersonalization") ?: false
                            _authEvents.emit(AuthEvents.Success)
                            flowOf(if(hasCompleted) AuthState.AUTHENTICATED else AuthState.PERSONALALIZATION_INCOMPLETE )
                        } else{
                            _authEvents.emit(AuthEvents.Success)
                            flowOf(AuthState.UNAUTHENTICATED)
                        }
                    } catch (e: Exception){
                        _authEvents.emit(AuthEvents.Error(e.message ?: "Something went wrong."))
                        flowOf(AuthState.UNAUTHENTICATED)
                    }
                }
            }
    }

    override suspend fun loginWithGoogle(cred: AuthCredential, email: String) {
        withContext(Dispatchers.IO){
            try {
                val query = firestore.collection("users").whereEqualTo("email", email).get(Source.SERVER).await()
                if(!query.isEmpty){
                    firebaseAuth.signInWithCredential(cred).await()
                    _triggerListenerState.value++
                } else {
                    _authEvents.emit(AuthEvents.Error("User does not exist."))
                }
            } catch (e : Exception){
                _authEvents.emit(AuthEvents.Error(e.message ?: "Something went wrong."))

            }

        }
    }

    override suspend fun registerWithGoogle(cred: AuthCredential, email: String) {
        withContext(Dispatchers.IO){
            try {
                val query = firestore.collection("users").whereEqualTo("email", email).get(Source.SERVER).await()
                if(query.isEmpty){
                    firebaseAuth.signInWithCredential(cred).await()
                    _triggerListenerState.value++
                } else {
                    _authEvents.emit(AuthEvents.Error("User already exists."))
                }
            } catch (e : Exception){
                  _authEvents.emit(AuthEvents.Error(e.message ?: "Something went wrong."))
            }
        }
    }

    override suspend fun createUserProfile() {
        withContext(Dispatchers.IO){
            try {
                val uid = firebaseAuth.currentUser?.uid ?: throw Exception("Uid is null.")
                val email = firebaseAuth.currentUser?.email ?: throw Exception("Email is null.")
                val userData = mapOf(
                    "email" to email,
                    "hasCompletedPersonalization" to false
                )
                firestore.collection("users").document(uid).set(userData).await()
                _triggerListenerState.value++
            } catch (e : Exception){
                  _authEvents.emit(AuthEvents.Error(e.message ?: "Something went wrong."))
            }
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

}