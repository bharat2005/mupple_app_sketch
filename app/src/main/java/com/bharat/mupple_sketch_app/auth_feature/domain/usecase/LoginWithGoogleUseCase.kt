package com.bharat.mupple_sketch_app.auth_feature.domain.usecase

import com.bharat.mupple_sketch_app.auth_feature.domain.repo.AuthRepo
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepo : AuthRepo
) {
    operator fun invoke(cred : AuthCredential) : Flow<Result<Unit>>{
        return authRepo.loginWithGoogle(cred)
    }
}