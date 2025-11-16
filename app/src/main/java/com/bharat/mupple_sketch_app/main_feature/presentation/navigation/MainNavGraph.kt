package com.bharat.mupple_sketch_app.main_feature.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bharat.mupple_sketch_app.app_root.AppRoutes
import com.bharat.mupple_sketch_app.main_feature.presentation.home.HomeScreen

fun NavGraphBuilder.mainNavGraph(navController: NavController){
    navigation(
        route = AppRoutes.MainRoute,
        startDestination = MainRoutes.HomeRoute
    ){
        composable(MainRoutes.HomeRoute) {
            HomeScreen()
        }
    }
}