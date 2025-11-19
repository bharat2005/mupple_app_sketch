package com.bharat.mupple_sketch_app.auth_feature.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthEvents
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

}

@HiltViewModel
class StartViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            authRepository.getAuthEvent().collect { event ->
                when(event){
                    is AuthEvents.Success -> {  }
                    is AuthEvents.Error -> { _uiState.update { LoginUiState.Error(event.message) } }
                }
            }
        }
    }


    fun setLoading(bool : Boolean){
        _uiState.update { if(bool) LoginUiState.Loading else LoginUiState.Idle }
    }

    fun onLocalError(error : String){
        _uiState.update { LoginUiState.Error(error) }
    }



    fun onErrorDismiss(){
        _uiState.update { LoginUiState.Idle }
    }


    fun onLocalGoogleSignInSuccess(idToken : String, email : String){
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            authRepository.loginWithGoogle(cred, email)
        }
    }




}