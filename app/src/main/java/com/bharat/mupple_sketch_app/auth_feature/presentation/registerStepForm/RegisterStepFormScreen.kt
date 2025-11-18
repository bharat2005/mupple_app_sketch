package com.bharat.mupple_sketch_app.auth_feature.presentation.registerStepForm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterStepFormScreen(
    viewModel: RegisterStepFormViewModel = hiltViewModel(),
    navigateBack : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val showExitDialog by viewModel.exitDialogState.collectAsState()

    BackHandler {
        viewModel.onBackPressed()
    }

    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Text("RegistrationStepform screen")

        Button(
            onClick = {
                viewModel.completeProfileCreation()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text("Complete Registeration")
        }
    }

    if(uiState.isLoading){
        Box(
            modifier = Modifier.fillMaxSize().pointerInput(Unit){},
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }

    if(uiState.profileCreationError != null){
        AlertDialog(
            onDismissRequest = {viewModel.onErrorDismiss()},
            title = {Text("Error")},
            text = {Text(uiState.profileCreationError ?: "")},
            confirmButton = {
                Button(onClick = {viewModel.onErrorDismiss()}) {Text("OK") }
            }
            )

}

    if(showExitDialog){
        AlertDialog(
            onDismissRequest = {viewModel.onExitDialogDismiss()},
            title = {Text("Alert")},
            text = {Text("Are you sure you want to go back?")},
            confirmButton = {
                Button(onClick = {
                    viewModel.onExitDialogConfirm()
                    navigateBack()
                }) {Text("Exit Anyway") }
            },
            dismissButton = {
                Button(onClick = {viewModel.onExitDialogDismiss()}) {Text("Cancel") }
            }
        )

    }

}