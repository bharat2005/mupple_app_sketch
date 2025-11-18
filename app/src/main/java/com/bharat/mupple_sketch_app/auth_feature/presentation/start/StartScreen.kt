package com.bharat.mupple_sketch_app.auth_feature.presentation.start

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


@Composable
fun StartScreen(
    viewModel : StartViewModel = hiltViewModel(),
    onGetStated : () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("372927817742-kjlr958f15j7dqdupa54qeetivc0nove.apps.googleusercontent.com")
            .build()
    }

    val gsc = remember{
        GoogleSignIn.getClient(context, gso)
    }

    val gsl = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken ?: throw Exception("Id Token got null.")
                val email = account?.email ?: throw Exception("Email got null.")
                viewModel.onLocalGoogleSignInSuccess(idToken, email)
            } catch (e : Exception){
                viewModel.setLoading(false)
                viewModel.onError(e.message ?: "Something went wrong.")
            }
        }else {
            viewModel.setLoading(false)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
        onClick = onGetStated
        ) {
            Text("Get STarted")
        }

        Button(
            onClick = {
                    viewModel.setLoading(true)
                    gsc.signOut().addOnCompleteListener {
                        gsl.launch(gsc.signInIntent)
                }
            }
        ) {
            Text("Login with Google")
        }
    }

    if(uiState.loginError != null){
        AlertDialog(
            onDismissRequest = {viewModel.onErrorDismiss()},
            title = { Text("Error") },
            text = {Text(uiState.loginError ?: "")},
            confirmButton = {
                Button(onClick = {
                    viewModel.onErrorDismiss()
                }) {Text("OK") }
            }
        )
    }

    if(uiState.isLoading){
        Box(
            modifier = Modifier.fillMaxSize().pointerInput(Unit){},
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
}