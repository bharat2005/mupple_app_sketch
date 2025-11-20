package com.bharat.mupple_sketch_app.auth_feature.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthOperationState
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed class LoginUiState{
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Error(val message : String) : LoginUiState()

    object Success : LoginUiState()

}

@HiltViewModel
class StartViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState = authRepository.getAuthOperationState().map { state ->
        when(state){
            is AuthOperationState.Idle -> LoginUiState.Idle
            is AuthOperationState.Loading -> LoginUiState.Loading
            is AuthOperationState.Success -> LoginUiState.Success
            is AuthOperationState.Error-> LoginUiState.Error(state.message)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LoginUiState.Idle)


    init {
        authRepository.clearAuthOperationState()
    }


    fun onErrorDismiss(){
        //_uiState.update { LoginUiState.Idle }
        authRepository.clearAuthOperationState()
    }


    fun onLocalGoogleSignInSuccess(idToken : String, email : String){
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            authRepository.loginWithGoogle(cred, email)
        }
    }




}