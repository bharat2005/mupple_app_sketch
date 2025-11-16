package com.bharat.mupple_sketch_app.auth_feature.domain.repo

import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    fun loginWithGoogle(cred : AuthCredential) : Flow<Result<Unit>>
}