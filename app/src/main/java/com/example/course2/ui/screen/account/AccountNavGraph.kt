package com.example.course2.ui.screen.account

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.course2.navigation.AppNavigation
import com.example.course2.ui.screen.account.auth.authScreenNavGraph

fun NavGraphBuilder.accountNavGraph() {
    navigation(
        route = AppNavigation.Account.route,
        startDestination = AppNavigation.Account.startDestination.route
    ) {
        authScreenNavGraph()

    }
}
