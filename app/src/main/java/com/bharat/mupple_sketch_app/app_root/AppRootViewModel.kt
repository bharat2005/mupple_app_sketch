package com.bharat.mupple_sketch_app.app_root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


enum class AuthState{
    UNKNOWN,
    UNAUTHENTICATED,
    PERSONALALIZATION_INCOMPLETE,
    AUTHENTICATED
}

sealed class AuthOperationState{
    object Idle  : AuthOperationState()
    object Loading : AuthOperationState()
    data class Error(val message: String?) : AuthOperationState()
    object Success : AuthOperationState()
}


@HiltViewModel
class AppRootViewModel @Inject constructor(
//    private val firebaseAuth : FirebaseAuth,
//    private val firestore : FirebaseFirestore,
//    private val triggerListenerFlag: TriggerListenerFlag,
//    private val authListenerFlag: AuthListenerFlag,
    private val authRepo: AuthRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

//    private val firebaseFlow = callbackFlow {
//        val listner = FirebaseAuth.AuthStateListener { firebaseAuth ->
//            trySend(  firebaseAuth)
//        }
//        firebaseAuth.addAuthStateListener(listner)
//        awaitClose {
//            firebaseAuth.removeAuthStateListener(listner)
//        }
//    }

    val authState = authRepo.getAuthState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        AuthState.UNAUTHENTICATED
    )

//    val authState = combine(
//        firebaseFlow,
//        triggerListenerFlag.triggerListenerState
//    ){ firebaseAuth,_ ->
//        firebaseAuth
//    }.flatMapLatest { firebaseAuth ->
//        try {
//            if (firebaseAuth.currentUser != null) {
//                val uid = firebaseAuth.currentUser?.uid ?: throw Exception("From AuthListener - Current User Id is null.")
//                    val userDoc = firestore.collection("users").document(uid).get(Source.SERVER).await()
//                    if (userDoc.exists()) {
//                        val hasOnboardingCompleted = userDoc.getBoolean("hasOnboardingComplete") ?: false
//                        if(hasOnboardingCompleted){
//                            authListenerFlag.onSuccess()
//                            flowOf(AuthState.AUTHENTICATED)
//                        } else {
//                            authListenerFlag.onSuccess()
//                            flowOf(AuthState.PERSONALALIZATION_INCOMPLETE)
//                        }
//                    } else {
//                        authListenerFlag.onSuccess()
//                        flowOf(AuthState.UNAUTHENTICATED)
//                    }
//                } else {
//                flowOf(AuthState.UNAUTHENTICATED)
//            }
//        } catch ( e : Exception){
//            authListenerFlag.onError("From AuthListener - ${e.message ?: "Something went wrong."}")
//            flowOf(AuthState.UNAUTHENTICATED)
//        }
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = AuthState.UNKNOWN
//        )
    val isOnline = networkMonitor.isOnline

}