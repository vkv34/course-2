package com.example.course2.ui.screen.loading

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.loadingScreenNavGraph(){
    composable(route = AppNavigation.Loading.route){
        LoadingScreen()
    }
}