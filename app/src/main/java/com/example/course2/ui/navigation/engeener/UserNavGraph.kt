package com.example.course2.ui.navigation.engeener

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.course2.navigation.AppNavigation
import com.example.course2.ui.screen.engineer.home.engineerHomeNavGraph
import com.example.course2.ui.screen.engineer.myRequests.engineerMyRequestsNavGraph
import com.example.course2.ui.screen.engineer.requestDetails.engineenerRequestDetailsNavGraph


fun NavGraphBuilder.engineerNavigation() {
    navigation(
        route = AppNavigation.Engeener.route,
        startDestination = AppNavigation.Engeener.HomeScreen.route
    ){
        engineerHomeNavGraph()
        engineenerRequestDetailsNavGraph()
        engineerMyRequestsNavGraph()
    }
}