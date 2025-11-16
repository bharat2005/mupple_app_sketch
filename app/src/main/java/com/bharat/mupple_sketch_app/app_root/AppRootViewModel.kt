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
    private val authInstance : FirebaseAuth
) : ViewModel() {

    private val firebaseFlow = callbackFlow {
        val listner = FirebaseAuth.AuthStateListener { currentUser ->
            trySend(currentUser)
        }
        authInstance.addAuthStateListener(listner)
        awaitClose {
            authInstance.removeAuthStateListener(listner)
        }
    }

    val authState = firebaseFlow
        .map { currentUser ->
            if(currentUser != null) AuthState.AUTHENTICATED else AuthState.AUTHENTICATED
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.UNKNOWN
        )







}