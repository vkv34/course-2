package com.example.course2.ui.screen.engineer.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.engineerHomeNavGraph() {
    composable(
        AppNavigation.Engeener.HomeScreen.route,
    ) {
        EngineerHomeScreen()
    }
}