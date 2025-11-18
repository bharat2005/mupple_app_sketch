package com.bharat.mupple_sketch_app.auth_feature.presentation.registerStepForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bharat.mupple_sketch_app.app_root.AuthEvents
import com.bharat.mupple_sketch_app.app_root.AuthListenerFlag
import com.bharat.mupple_sketch_app.app_root.AuthOperationState
import com.bharat.mupple_sketch_app.app_root.AuthState
import com.bharat.mupple_sketch_app.app_root.TriggerListenerFlag
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.ProfileCreationUseCase
import com.bharat.mupple_sketch_app.auth_feature.domain.usecase.SignOutUseCase
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepo
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class StepFormState(
    val isLoading : Boolean = false,
    val profileCreationError : String? = null
)

@HiltViewModel
class RegisterStepFormViewModel @Inject constructor(
    private val profileCreationUseCase: ProfileCreationUseCase,
    private val triggerListenerFlag: TriggerListenerFlag,
    private val signOutUseCase: SignOutUseCase,
    private val authListenerFlag: AuthListenerFlag,
    private val authRepo: AuthRepo
): ViewModel() {

    val uiState = authRepo.getAuthOperationState().map { operationState ->
        StepFormState(
            isLoading = operationState is AuthOperationState.Loading,
            profileCreationError = (operationState as? AuthOperationState.Error)?.message
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StepFormState()
    )

    private val _exitDialogState = MutableStateFlow(false)
    val exitDialogState = _exitDialogState.asStateFlow()



//    private val _uiState = MutableStateFlow(StepFormState())
//    val uiState = _uiState.asStateFlow()
//
    fun onErrorDismiss(){
        authRepo.clearAuthOperationState()
//        _uiState.update { it.copy(profileCreationError = null) }
    }
//
    fun onBackPressed(){
        _exitDialogState.update { true }
    }
//
    fun onExitDialogDismiss(){
        _exitDialogState.update { false }
    }
//
    fun onExitDialogConfirm(){
        authRepo.signOut()
        _exitDialogState.update { false }
    }


    fun completeProfileCreation(){
//        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
//          profileCreationUseCase().collect { result ->
//              result.fold(
//                  onSuccess = { triggerListenerFlag.trigger()  },
//                  onFailure = { e ->
//                      _uiState.update { it.copy(isLoading = false, profileCreationError = e.message) }
//                  }
//              )
//          }

        }
    }


//    init {
//        viewModelScope.launch {
//            authListenerFlag.authEvents.collect { event ->
//                when(event){
//                    is AuthEvents.Success -> { _uiState.update { it.copy(profileCreationError = null) }}
//                    is AuthEvents.Error -> {  _uiState.update { it.copy(isLoading = false, profileCreationError = event.error) }  }
//                }
//            }
//        }
//    }







}