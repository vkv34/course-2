package com.example.course2.ui.screen.engineer.myRequests

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.engineerMyRequestsNavGraph() {
    composable(AppNavigation.Engeener.MyRequestsScreen.route) {
        MyRequestsScreen()
    }
}