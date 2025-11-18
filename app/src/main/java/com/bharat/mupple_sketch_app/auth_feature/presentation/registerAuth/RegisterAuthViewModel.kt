package com.bharat.mupple_sketch_app.auth_feature.presentation.registerAuth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.app_root.AuthEvents
import com.bharat.mupple_sketch_app.app_root.AuthListenerFlag
import com.bharat.mupple_sketch_app.app_root.AuthOperationState
import com.bharat.mupple_sketch_app.app_root.TriggerListenerFlag
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.RegisterWithGoogleUseCase
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
import kotlin.math.log

data class RegisterUiState(
    val isLoading : Boolean = false,
    val registerError : String? = null,
    val registerSuccess : Boolean = false
)

@HiltViewModel
class RegisterAuthViewModel @Inject constructor(
    private val registerWithGoogleUseCase: RegisterWithGoogleUseCase,
    private val authListenerFlag: AuthListenerFlag,
    private val triggerListenerFlag: TriggerListenerFlag,
    private val authRepo: AuthRepo
) : ViewModel() {

//    private val _uiState = MutableStateFlow(RegisterUiState())
//    val uiState = _uiState.asStateFlow()

    val uiState = authRepo.getAuthOperationState().map { operationState ->
        RegisterUiState(
            isLoading = operationState is AuthOperationState.Loading,
            registerError = (operationState as? AuthOperationState.Error)?.message,
            registerSuccess = operationState is AuthOperationState.Success
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        RegisterUiState()
    )




//    fun setLoading(isLoading : Boolean){
//        _uiState.update { it.copy(isLoading = isLoading) }
//    }
//
//    fun onRegisterError(error : String){
//        _uiState.update { it.copy(registerError = error) }
//    }
//
    fun onErrorDismiss(){
//        _uiState.update { it.copy(registerError = null) }
        authRepo.clearAuthOperationState()
    }
//
//    fun onRemoveRegisterSuccesFlag(){
//        _uiState.update { it.copy(registerSuccess = false) }
//    }
    fun onLocalGoogleRegisterSuccess(idToken : String, email : String){
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            authRepo.registerWithGoogle(cred, email)
//            registerWithGoogleUseCase(cred, email).collect { result ->
//                result.fold(
//                    onSuccess = { triggerListenerFlag.trigger() },
//                    onFailure = { e->
//                        _uiState.update { it.copy(isLoading = false, registerError = e.message) }
//                    }
//                )
//            }
        }
    }


//    init {
//        viewModelScope.launch {
//            authListenerFlag.authEvents.collect { event ->
//                when(event){
//                    is AuthEvents.Success -> { _uiState.update { it.copy(registerSuccess = true, registerError = null) }}
//                    is AuthEvents.Error -> {  _uiState.update { it.copy(isLoading = false, registerError = event.error) } }
//                }
//            }
//        }
//    }





}