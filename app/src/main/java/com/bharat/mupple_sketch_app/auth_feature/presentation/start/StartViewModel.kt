package com.bharat.mupple_sketch_app.auth_feature.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.app_root.AuthEvents
import com.bharat.mupple_sketch_app.app_root.AuthListenerFlag
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.LoginWithGoogleUseCase
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginUiState(
    val isLoading : Boolean = false,
    val loginError : String? = null
)
@HiltViewModel
class StartViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val authListenerFlag: AuthListenerFlag
) : ViewModel() {



    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onError(error : String){
        _uiState.update { it.copy(
            loginError = error
        ) }
    }

    fun setLoading(isLoading : Boolean){
        _uiState.update { it.copy(isLoading = isLoading) }
    }


    fun onErrorDismiss(){
        _uiState.update { it.copy(loginError =  null) }
    }


    fun onLocalGoogleSignInSuccess(idToken : String, email : String){
         val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            loginWithGoogleUseCase(cred, email).collect { result ->
                result.fold(
                    onSuccess = { },
                    onFailure = { e -> _uiState.update { it.copy(isLoading = false, loginError = e.message) } }
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            authListenerFlag.authEvents.collect { event ->
                    when(event){
                        is AuthEvents.Success -> { _uiState.update { it.copy(isLoading = false, loginError = null) }}
                        is AuthEvents.Error -> { _uiState.update { it.copy(isLoading = false, loginError = event.error) }}
                    }
            }
        }
    }







}