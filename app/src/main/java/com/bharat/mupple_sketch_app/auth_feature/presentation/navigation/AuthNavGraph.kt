package com.bharat.mupple_sketch_app.auth_feature.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bharat.mupple_sketch_app.app_root.AppRoutes
import com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth.CollegeAuthScreen
import com.bharat.mupple_sketch_app.auth_feature.presentation.registerAuth.RegisterAuthAScreen
import com.bharat.mupple_sketch_app.auth_feature.presentation.registerStepForm.RegisterStepFormScreen
import com.bharat.mupple_sketch_app.auth_feature.presentation.start.StartScreen

fun NavGraphBuilder.authNavGraph(navController: NavController){
    navigation(
        route = AppRoutes.AuthRoute,
        startDestination = AuthRoutes.CollegeAuth
    ){
        composable(AuthRoutes.StartRoute) {
            StartScreen(
                onGetStated = {navController.navigate(AuthRoutes.RegisterAuth){launchSingleTop = true} }
            )
        }

        composable(AuthRoutes.RegisterAuth) {
            RegisterAuthAScreen(
                onExit = {navController.navigateUp()},
                onRegisterSuccess = {navController.navigate(AuthRoutes.RegisterStepForm){launchSingleTop= true} }
            )
        }

        composable(AuthRoutes.CollegeAuth) {
            CollegeAuthScreen(

            )
        }

        composable(AuthRoutes.RegisterStepForm) {
            RegisterStepFormScreen(
                navigateBack = {navController.navigate(AuthRoutes.RegisterAuth){
                    popUpTo(AuthRoutes.RegisterAuth){inclusive = true}
                } }

            )
        }
    }
}