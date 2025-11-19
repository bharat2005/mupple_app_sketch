package com.bharat.mupple_sketch_app.auth_feature.presentation.registerAuth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthEvents
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
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


data class RegisterUiState(
    val isLoading : Boolean = false,
    val registerSuccess : Boolean = false,
    val registerError : String? = null
)

@HiltViewModel
class RegisterAuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()


    init{
        viewModelScope.launch {
            authRepository.getAuthEvent().collect { event ->
                when(event){
                    is AuthEvents.Success -> { _uiState.update { it.copy(registerSuccess = true, registerError = null) } }
                    is AuthEvents.Error -> { _uiState.update { it.copy(isLoading = false, registerSuccess = false, registerError = event.message) } }
                }
            }
        }
    }

    fun setLoading(bool : Boolean){
        _uiState.update { it.copy(isLoading = bool, registerError = null) }
    }

    fun onError(error : String){
        _uiState.update { it.copy(isLoading = false, registerError = error) }
    }
    fun onErrorDismiss(){
        _uiState.update  { it.copy(registerError = null) }
    }


    fun onLocalGoogleRegisterSuccess(idToken : String, email : String){
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            authRepository.registerWithGoogle(cred, email)
        }
    }




}