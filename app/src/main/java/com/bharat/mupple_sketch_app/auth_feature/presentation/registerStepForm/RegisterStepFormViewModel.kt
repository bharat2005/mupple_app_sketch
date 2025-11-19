package com.bharat.mupple_sketch_app.auth_feature.presentation.registerStepForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthEvents
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class StepFormState(
    val isLoading : Boolean = false,
    val showExitDialog : Boolean = false   ,
    val profileCreationError : String? = null
)

@HiltViewModel
class RegisterStepFormViewModel @Inject constructor(
    private  val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(StepFormState())
    val uiState = _uiState.asStateFlow()

    fun onErrorDismiss(){
        _uiState.update { it.copy(profileCreationError = null) }
    }

    fun onBackPressed(){
        _uiState.update { it.copy(showExitDialog = true) }
    }

    fun onExitDialogDismiss(){
        _uiState.update { it.copy(showExitDialog = false) }
    }

    fun onExitDialogConfirm(){
        authRepository.signOut()
        _uiState.update { it.copy(showExitDialog = false) }
    }


    fun completeProfileCreation(){
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.createUserProfile()
        }
    }


    init {
        viewModelScope.launch {
            authRepository.getAuthEvents().collect { event ->
                when(event){
                    is AuthEvents.Success ->{ _uiState.update { it.copy(profileCreationError = null) } }
                    is AuthEvents.Error -> {   _uiState.update { it.copy(isLoading = false, profileCreationError = event.message) } }
                }
            }
        }
    }





}