package com.bharat.mupple_sketch_app.auth_feature.data.repo

import com.bharat.mupple_sketch_app.auth_feature.domain.repo.AuthRepo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoIml @Inject constructor(
    private val authInstance : FirebaseAuth
)  : AuthRepo {
    override fun loginWithGoogle(cred: AuthCredential): Flow<Result<Unit>> {
        return flow {
            authInstance.signInWithCredential(cred).await()
            emit(Result.success(Unit))
        }.catch {
            emit(Result.failure(it))
        }
    }
}