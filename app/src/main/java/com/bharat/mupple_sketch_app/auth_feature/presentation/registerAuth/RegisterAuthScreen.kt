package com.bharat.mupple_sketch_app.auth_feature.presentation.registerAuth


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun RegisterAuthAScreen(
    onExit: () -> Unit,
    viewModel: RegisterAuthViewModel = hiltViewModel(),
    onRegisterSuccess : () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()


    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("372927817742-kjlr958f15j7dqdupa54qeetivc0nove.apps.googleusercontent.com")
            .build()
    }

    val gsc = remember {
        GoogleSignIn.getClient(context, gso )
    }

    val gsl = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken ?: throw Exception("Id token not found.")
                val email = account?.email ?: throw Exception("Email not found.")
                viewModel.onLocalGoogleRegisterSuccess(idToken, email)

            }catch (e : Exception){
               // viewModel.onError(e.message ?: "Something went wrong.")
            }
        } else {
         //   viewModel.setLoading(false)
        }
    }

    LaunchedEffect(uiState) {
        if(uiState is RegisterUiState.Success){
            onRegisterSuccess()
        }
    }




    Scaffold(
        topBar = {RegisterAuthScreenTopAppBar(onExit = onExit)},
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                   // viewModel.setLoading(true)
                    gsc.signOut().addOnCompleteListener {
                        gsl.launch(gsc.signInIntent)
                    }
                }
            ) {
                Text("Register with Google")
            }

        }
    }

    if(uiState is RegisterUiState.Error){
        AlertDialog(
            onDismissRequest = {viewModel.onErrorDismiss()},
            title = {Text("Error")},
            text = {Text((uiState as RegisterUiState.Error).message)},
            confirmButton = {
                Button(onClick = {viewModel.onErrorDismiss()}) {Text("OK") }
            }
        )

    }

    if(uiState is RegisterUiState.Loading){
        Box(
            modifier = Modifier.fillMaxSize().pointerInput(Unit){},
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterAuthScreenTopAppBar(
    onExit : () -> Unit
){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title={},
        navigationIcon = {
            IconButton(onClick = onExit) { Icon(Icons.Default.ArrowBack, null) }
        }
    )
}