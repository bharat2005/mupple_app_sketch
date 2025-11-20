package com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


enum class VerificationStep{
    SELECT_COLLEGE,
    ENTER_EMAIL,
    VERIFY_OTP,
}

data class CollegeAuthUiState(
    val currentStep : VerificationStep = VerificationStep.SELECT_COLLEGE
)

@HiltViewModel
class CollegeAuthViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(CollegeAuthUiState())
    val uiState = _uiState.asStateFlow()


    fun goToStep(step : VerificationStep){
        _uiState.update { it.copy(currentStep = step) }
    }
    fun goToPrev(){
        val prevStep = when(uiState.value.currentStep){
            VerificationStep.SELECT_COLLEGE -> null
            VerificationStep.ENTER_EMAIL -> VerificationStep.SELECT_COLLEGE
            VerificationStep.VERIFY_OTP -> VerificationStep.ENTER_EMAIL
        }
        if(prevStep != null){
            _uiState.update { it.copy(currentStep = prevStep) }
        }
    }

}





















