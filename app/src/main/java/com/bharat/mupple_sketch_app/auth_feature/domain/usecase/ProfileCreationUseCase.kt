package com.bharat.mupple_sketch_app.auth_feature.domain.usecase

import com.bharat.mupple_sketch_app.auth_feature.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileCreationUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    operator fun invoke() : Flow<Result<Unit>>{
        return authRepo.saveUserDetails()
    }
}