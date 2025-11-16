package com.bharat.mupple_sketch_app.auth_feature.domain.usecase

import com.bharat.mupple_sketch_app.auth_feature.domain.repo.AuthRepo
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    operator fun invoke() : Unit = authRepo.signOut()

}