package com.bharat.mupple_sketch_app.personalization_feature.data.repo

import com.bharat.mupple_sketch_app.personalization_feature.domain.PersonalizationRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PersonalizationRepoIml @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth : FirebaseAuth
) : PersonalizationRepo {

    override fun savePersonalizationDetails(): Flow<Result<Unit>> {
        return flow {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("Uid is null.")
            firestore.collection("users").document(uid).update("hasOnboardingComplete", true)
                .await()
            emit(Result.success(Unit))

        }.catch { e ->
            emit(Result.failure(Exception(e.message)))
        }
    }

}