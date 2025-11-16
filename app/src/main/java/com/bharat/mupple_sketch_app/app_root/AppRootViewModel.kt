package com.bharat.mupple_sketch_app.app_root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

enum class AuthState{
    UNKNOWN,
    UNAUTHENTICATED,
    AUTHENTICATED
}

@HiltViewModel
class AppRootViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : ViewModel() {

    private val firebaseFlow = callbackFlow {
        val listner = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(  if(firebaseAuth.currentUser != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED)
        }
        firebaseAuth.addAuthStateListener(listner)
        awaitClose {
            firebaseAuth.removeAuthStateListener(listner)
        }
    }

    val authState = firebaseFlow
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.UNKNOWN
        )

}