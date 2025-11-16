package com.bharat.mupple_sketch_app.app_root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.bharat.mupple_sketch_app.app_root.splash.SplashScreen

@Composable
fun AppRoot(
    viewModel: AppRootViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val startDestination = when(authState){
        AuthState.UNKNOWN -> AppRoutes.SplashRoute
        AuthState.UNAUTHENTICATED -> AppRoutes.AuthRoute
        AuthState.AUTHENTICATED -> AppRoutes.MainRoute
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





        }
    }

}