package com.bharat.mupple_sketch_app.personalization_feature.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.bharat.mupple_sketch_app.app_root.AppRoutes

fun NavGraphBuilder.personalizationNavGraph(navController: NavController){
    navigation(
        route = AppRoutes.PersonalizationRoute,
        startDestination = "Ap"
    )
}