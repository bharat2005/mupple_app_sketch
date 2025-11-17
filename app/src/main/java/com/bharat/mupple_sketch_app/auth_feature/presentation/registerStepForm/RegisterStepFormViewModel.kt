package com.bharat.mupple_sketch_app.auth_feature.presentation.registerStepForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bharat.mupple_sketch_app.app_root.TriggerListenerFlag
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.ProfileCreationUseCase
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.SignOutUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class StepFormState(
    val isLoading : Boolean = false,
    val showExitDialog : Boolean = false   ,
    val profileCreationError : String? = null
)

@HiltViewModel
class RegisterStepFormViewModel @Inject constructor(
    private val profileCreationUseCase: ProfileCreationUseCase,
    private val triggerListenerFlag: TriggerListenerFlag,
    private val signOutUseCase: SignOutUseCase,
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
        signOutUseCase()
        _uiState.update { it.copy(showExitDialog = false) }
    }


    fun completeProfileCreation(){
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
          profileCreationUseCase().collect { result ->
              result.fold(
                  onSuccess = {
                      _uiState.update { it.copy(isLoading = false) }
                      triggerListenerFlag.trigger()
                  },
                  onFailure = { e ->
                      _uiState.update { it.copy(isLoading = false, profileCreationError = e.message) }
                  }
              )
          }

        }
    }






}