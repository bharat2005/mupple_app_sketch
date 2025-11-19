package com.bharat.mupple_sketch_app.app_root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.bharat.mupple_sketch_app.app_root.splash.SplashScreen
import com.bharat.mupple_sketch_app.auth_feature.presentation.navigation.authNavGraph
import com.bharat.mupple_sketch_app.core.data.repo.AuthState
import com.bharat.mupple_sketch_app.main_feature.presentation.navigation.mainNavGraph
import com.bharat.mupple_sketch_app.personalization_feature.presentation.navigation.personalizationNavGraph

@Composable
fun AppRoot(
    viewModel: AppRootViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsState()
    var showInternetDialog by remember { mutableStateOf(true) }

    val startDestination = when(authState){
        AuthState.UNKNOWN -> AppRoutes.SplashRoute
        AuthState.UNAUTHENTICATED -> AppRoutes.AuthRoute
        AuthState.PERSONALALIZATION_INCOMPLETE -> AppRoutes.PersonalizationRoute
        AuthState.AUTHENTICATED -> AppRoutes.MainRoute
    }

    LaunchedEffect(isOnline, showInternetDialog) {
        showInternetDialog = !isOnline
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        NavHost(
            navController = navController,
            startDestination = startDestination
        ){
            composable(AppRoutes.SplashRoute) {
                SplashScreen()
            }

            authNavGraph(navController)

            personalizationNavGraph(navController)

            mainNavGraph(navController)

        }

        if(showInternetDialog){
            AlertDialog(
                onDismissRequest = {},
                text = { Text("make the internet on") },
                title = {Text("Alert")},
                confirmButton = {
                    Button(onClick = {
                        showInternetDialog = false
                    }) {
                        Text("Retry")
                    }
                }
            )
        }
    }

}