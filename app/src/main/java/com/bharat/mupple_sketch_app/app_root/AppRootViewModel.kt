package com.bharat.mupple_sketch_app.app_root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    AUTHENTICATED
}

@HiltViewModel
class AppRootViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val triggerListenerFlag: TriggerListenerFlag,
) : ViewModel() {

    private val firebaseFlow = callbackFlow {
        val listner = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(  firebaseAuth)
        }
        firebaseAuth.addAuthStateListener(listner)
        awaitClose {
            firebaseAuth.removeAuthStateListener(listner)
        }
    }

    val authState = combine(
        firebaseFlow,
        triggerListenerFlag.triggerListenerState
    ){ firebaseAuth,_ ->
        firebaseAuth
    }.flatMapLatest { firebaseAuth ->
                if (firebaseAuth.currentUser != null) {
                    val uid = firebaseAuth.currentUser?.uid
                    if (uid != null) {
                        val userDoc = firestore.collection("users").document(uid!!).get().await()
                        if (userDoc.exists()) {
                            flowOf(firebaseAuth)
                        } else {
                            flowOf(null)
                        }
                    } else {
                        flowOf(null)
                    }
                } else {
                    flowOf(null)
                }
        }
        .map {
            if(it != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.UNKNOWN
        )

}