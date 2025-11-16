package com.bharat.mupple_sketch_app.auth_feature.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.LoginWithGoogleUseCase
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoggingIn : Boolean = false,
    val loginSuccess : Boolean = false,
    val loginError : String? = null
)
@HiltViewModel
class StartViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
 ) : ViewModel() {

     private val _uiState = MutableStateFlow(LoginUiState())
     val uiState = _uiState.asStateFlow()

    fun onLoginError(error : String){
        _uiState.update { it.copy(loginError =  error) }
    }

    fun onErrorDissMiss(){
        _uiState.update { it.copy(loginError = null) }
    }

    fun setLoading(isLoading : Boolean){
        _uiState.update { it.copy(isLoggingIn = isLoading) }
    }

    fun onLocalGoogleSignInSuccess(idToken: String){
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            loginWithGoogleUseCase(cred).collect { result ->
                result.fold(
                    onSuccess = {

                    },
                    onFailure = { e -> onLoginError(e.message ?: "Unknown Error") }
                )
            }

        }
    }
 }