package com.bharat.mupple_sketch_app.core.domain.repo


import com.bharat.mupple_sketch_app.core.data.repo.AuthEvents
import com.bharat.mupple_sketch_app.core.data.repo.AuthState
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getAuthEvents() : Flow<AuthEvents>
    fun getAuthState(): Flow<AuthState>

    suspend fun loginWithGoogle(cred : AuthCredential, email : String)

    suspend fun registerWithGoogle(cred : AuthCredential, email : String)

    suspend fun createUserProfile()

    fun signOut()

}