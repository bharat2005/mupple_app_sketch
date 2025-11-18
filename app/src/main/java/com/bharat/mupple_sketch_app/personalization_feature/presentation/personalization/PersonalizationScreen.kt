package com.bharat.mupple_sketch_app.personalization_feature.presentation.personalization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PersonalizationScreen(
    viewModel: PersonalizationViewModel = hiltViewModel(),
    onPersonalizationSuccess : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.personalizationSuccess) {
        if(uiState.personalizationSuccess){
            onPersonalizationSuccess()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Button(
            onClick = {
                viewModel.onSubmitPersonalization()
            }
        ) {
            Text("Complete Personalization")
        }

    }

}