package com.bharat.mupple_sketch_app.auth_feature.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.app_root.AuthEvents
import com.bharat.mupple_sketch_app.app_root.AuthListenerFlag
import com.bharat.mupple_sketch_app.app_root.AuthOperationState
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.LoginWithGoogleUseCase
import com.bharat.mupple_sketch_app.auth_feature.presentation.registerAuth.RegisterUiState
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepo
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


data class LoginUiState(
    val isLoading : Boolean = false,
    val loginError : String? = null
)
@HiltViewModel
class StartViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val authListenerFlag: AuthListenerFlag,
    private val authRepo: AuthRepo
) : ViewModel() {



//    private val _uiState = MutableStateFlow(LoginUiState())
     val uiState = authRepo.getAuthOperationState().map { operationState ->
    LoginUiState(
        isLoading = operationState is AuthOperationState.Loading,
        loginError = (operationState as? AuthOperationState.Error)?.message
    )
}.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000L),
    LoginUiState()
)
//    val uiState = _uiState.asStateFlow()

//    fun onError(error : String){
//        _uiState.update { it.copy(
//            loginError = error
//        ) }
//    }
//
//    fun setLoading(isLoading : Boolean){
//        _uiState.update { it.copy(isLoading = isLoading) }
//    }
//
//
    fun onErrorDismiss(){
//        _uiState.update { it.copy(loginError =  null) }
        authRepo.clearAuthOperationState()
    }


    fun onLocalGoogleSignInSuccess(idToken : String, email : String){
         val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            authRepo.loginWithGoogle(cred, email)
//            loginWithGoogleUseCase(cred, email).collect { result ->
//                result.fold(
//                    onSuccess = {},
//                    onFailure = { e -> _uiState.update { it.copy(isLoading = false, loginError = e.message) } }
//                )
//            }
        }
    }

//    init {
//        viewModelScope.launch {
//            authListenerFlag.authEvents.collect { event ->
//                    when(event){
//                        is AuthEvents.Success -> { _uiState.update { it.copy(loginError = null) }}
//                        is AuthEvents.Error -> { _uiState.update { it.copy(isLoading = false, loginError = event.error) }}
//                    }
//            }
//        }
//    }



}