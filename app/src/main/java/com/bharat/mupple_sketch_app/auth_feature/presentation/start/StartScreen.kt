package com.bharat.mupple_sketch_app.auth_feature.presentation.start


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun StartScreen(
    viewModel: StartViewModel = hiltViewModel()
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
                val idToken = account?.idToken ?: throw Exception("ID Token is Null!")
                viewModel.onLocalGoogleSignInSuccess(idToken)
            } catch (e : Exception){
                viewModel.onLoginError(e.message ?: "Somethng went wrnng")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Button(
            onClick = {
                viewModel.setLoading(true)
                gsc.signOut().addOnCompleteListener {
                    gsl.launch(gsc.signInIntent)
                }
            }
        ) {
            Text("SignIn with Google")
        }

    }

    if(uiState.isLoggingIn){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit){},
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }

    if(uiState.loginError != null){
        AlertDialog(
            onDismissRequest = {viewModel.onErrorDissMiss()},
            title = {Text("Error")},
            text = {Text(uiState.loginError ?: "")},
            confirmButton = {
                Button(onClick = {viewModel.onErrorDissMiss()}) {Text("Ok") }
            }
        )
    }

}