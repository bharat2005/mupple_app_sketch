package com.bharat.mupple_sketch_app.auth_feature.presentation.registerAuth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthOperationState
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed class RegisterUiState{
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Error(val message : String) : RegisterUiState()
    object Success : RegisterUiState()

}

@HiltViewModel
class RegisterAuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState = authRepository.getAuthOperationState().map { state ->
        when(state){
            is AuthOperationState.Idle -> RegisterUiState.Idle
            is AuthOperationState.Loading -> RegisterUiState.Loading
            is AuthOperationState.Error -> RegisterUiState.Error(state.message)
            is AuthOperationState.Success -> RegisterUiState.Success
        }
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RegisterUiState.Idle)

init {
    authRepository.clearAuthOperationState()
}


    fun onErrorDismiss(){
        authRepository.clearAuthOperationState()
    }


    fun onLocalGoogleRegisterSuccess(idToken : String, email : String){
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            authRepository.registerWithGoogle(cred, email)
        }
    }




}