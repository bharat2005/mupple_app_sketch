package com.bharat.mupple_sketch_app.auth_feature.presentation.registerStepForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthOperationState
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import com.google.android.gms.auth.api.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class StepFormUiState{
    object Idle : StepFormUiState()
    object Loading : StepFormUiState()
    data class Error(val message : String) : StepFormUiState()
    object Success : StepFormUiState()
}

@HiltViewModel
class RegisterStepFormViewModel @Inject constructor(
    private  val authRepository: AuthRepository
): ViewModel() {

    val uiState = authRepository.getAuthOperationState().map{state ->
        when(state){
            is AuthOperationState.Idle -> StepFormUiState.Idle
            is AuthOperationState.Loading -> StepFormUiState.Loading
            is AuthOperationState.Error -> StepFormUiState.Error(state.message)
            is AuthOperationState.Success -> StepFormUiState.Success
        }
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepFormUiState.Idle)


    init {
        authRepository.clearAuthOperationState()
    }

    private val _showExitDialog = MutableStateFlow(false)
    val showExitDialog = _showExitDialog.asStateFlow()


    fun onErrorDismiss(){
       // _uiState.update { StepFormUiState.Idle }
        authRepository.clearAuthOperationState()
    }

    fun onBackPressed(){
        _showExitDialog.update { true }
    }

    fun onExitDialogDismiss(){
        _showExitDialog.update { false }
    }

    fun onExitDialogConfirm(){
        authRepository.signOut()
        _showExitDialog.update { false }
    }



    fun completeProfileCreation(){
      //  _uiState.update { StepFormUiState.Loading }
        viewModelScope.launch {
            authRepository.createUserProfile()
        }
    }




}