package com.bharat.mupple_sketch_app.auth_feature.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bharat.mupple_sketch_app.app_root.AppRoutes
import com.bharat.mupple_sketch_app.auth_feature.presentation.start.StartScreen

fun NavGraphBuilder.authNavGraph(navController: NavController){
    navigation(
        route = AppRoutes.AuthRoute,
        startDestination = AuthRoutes.StartRoute
    ){
        composable(AuthRoutes.StartRoute) {
            StartScreen()
        }
    }
}