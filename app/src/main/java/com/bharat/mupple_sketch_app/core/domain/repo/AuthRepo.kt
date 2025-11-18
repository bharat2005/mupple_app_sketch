package com.bharat.mupple_sketch_app.core.domain.repo

import com.bharat.mupple_sketch_app.app_root.AuthOperationState
import com.bharat.mupple_sketch_app.app_root.AuthState
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepo {
    fun getAuthState() : Flow<AuthState>

    fun getAuthOperationState() : StateFlow<AuthOperationState>

    suspend fun loginWithGoogle(cred : AuthCredential, email : String)

    suspend fun registerWithGoogle(cred: AuthCredential, email : String)

    suspend fun createUserProfile()

    fun clearAuthOperationState()

    fun signOut()

}