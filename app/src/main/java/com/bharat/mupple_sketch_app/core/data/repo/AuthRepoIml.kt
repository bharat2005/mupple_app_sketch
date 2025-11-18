package com.bharat.mupple_sketch_app.core.data.repo

import com.bharat.mupple_sketch_app.app_root.AuthOperationState
import com.bharat.mupple_sketch_app.app_root.AuthState
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepo
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoIml @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val firestore : FirebaseFirestore

): AuthRepo {
    val _authOperationState = MutableStateFlow<AuthOperationState>(AuthOperationState.Idle)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAuthState(): Flow<AuthState> {
        val firebaseFlow = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener{ firebaseAuth ->
                trySend(firebaseAuth)
            }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }
        return firebaseFlow
            .flatMapLatest { firebaseAuth ->
                val user = firebaseAuth.currentUser
            if(user == null){
                flowOf(AuthState.UNAUTHENTICATED)
            } else {
                try {
                    val userDocument = firestore.collection("users").document(user.uid).get(Source.SERVER).await()
                    if(userDocument.exists()){
                        val hasPersonalizationCompleted = userDocument.getBoolean("hasPersonalizationComplete") ?: false
                        if(hasPersonalizationCompleted){
                            _authOperationState.value= AuthOperationState.Success
                            flowOf(AuthState.AUTHENTICATED)
                        } else {
                            _authOperationState.value= AuthOperationState.Success
                            flowOf(AuthState.PERSONALALIZATION_INCOMPLETE)
                        }

                    } else {
                        _authOperationState.value = AuthOperationState.Success
                        flowOf(AuthState.UNAUTHENTICATED)
                    }

                } catch (e : Exception){
                    _authOperationState.value = AuthOperationState.Error(e.message)
                    flowOf(AuthState.UNAUTHENTICATED)
                }


            }
        }

    }

    override fun getAuthOperationState(): StateFlow<AuthOperationState> {
        return _authOperationState.asStateFlow()
    }

    override suspend fun loginWithGoogle(cred: AuthCredential, email: String) {
        withContext(Dispatchers.IO){
            _authOperationState.value = AuthOperationState.Loading
            try {
                val query = firestore.collection("users").whereEqualTo("email", email).get(Source.SERVER).await()
                if(!query.isEmpty){
                    firebaseAuth.signInWithCredential(cred).await()
                    _authOperationState.value = AuthOperationState.Success
                } else {
                    throw Exception("User does not exists.")
                }
            } catch (e : Exception){
                _authOperationState.value = AuthOperationState.Error(e.message ?: "Something went wrong.")
            }

        }

    }

    override suspend fun registerWithGoogle(cred: AuthCredential, email: String) {
        withContext(Dispatchers.IO){
            _authOperationState.value = AuthOperationState.Loading
            try {
                val query = firestore.collection("users").whereEqualTo("email", email).get(Source.SERVER).await()
                if(query.isEmpty){
                    firebaseAuth.signInWithCredential(cred).await()
                    _authOperationState.value = AuthOperationState.Success
                } else {
                   throw Exception("User already Exists.")
                }
            } catch (e : Exception){
                _authOperationState.value = AuthOperationState.Error(e.message ?: "Something went wrong.")
            }

        }

    }

    override suspend fun createUserProfile() {
        withContext(Dispatchers.IO){
            _authOperationState.value = AuthOperationState.Loading
            try {
                val uid = firebaseAuth.currentUser?.uid ?: throw Exception("Uid is null.")
                val data = mapOf(
                    "email" to firebaseAuth.currentUser?.email,
                    "hasPersonalizationComplete" to false
                )
                firestore.collection("users").document(uid).set(data).await()
                _authOperationState.value = AuthOperationState.Success
            } catch (e : Exception){
                _authOperationState.value = AuthOperationState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    override fun clearAuthOperationState() {
        _authOperationState.value = AuthOperationState.Idle
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

}