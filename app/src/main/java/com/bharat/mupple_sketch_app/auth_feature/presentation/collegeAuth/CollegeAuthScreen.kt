package com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth.components.Step1_SelectCollege
import com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth.components.Step2_EnterEmail
import com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth.components.Step3_VerifyOtp

@Composable
fun CollegeAuthScreen(
    viewModel: CollegeAuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        if(uiState.currentStep == VerificationStep.SELECT_COLLEGE){

        } else {
            viewModel.goToPrev()
        }
    }

        AnimatedContent(
            targetState = uiState.currentStep,
            transitionSpec = {
                if(targetState.ordinal > initialState.ordinal){
                    slideInHorizontally(initialOffsetX = {it}) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = {-it}) + fadeOut()
                } else {
                    slideInHorizontally(initialOffsetX = {-it}) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = {it}) + fadeOut()
                }
            }
        ) { targetState ->
            when(targetState){
                VerificationStep.SELECT_COLLEGE -> Step1_SelectCollege(
                    onCollegeSelected = {viewModel.goToStep(VerificationStep.ENTER_EMAIL)},
                    onBackClicked = {viewModel.goToPrev()}
                )
                VerificationStep.ENTER_EMAIL -> Step2_EnterEmail(
                    onSendOtpClicked = {viewModel.goToStep(VerificationStep.VERIFY_OTP)},
                    onBackClicked = {viewModel.goToPrev()}

                )
                VerificationStep.VERIFY_OTP -> Step3_VerifyOtp(
                    onVerifyClicked = {},
                    onBackClicked = {viewModel.goToPrev()}
                )
            }


    }

}