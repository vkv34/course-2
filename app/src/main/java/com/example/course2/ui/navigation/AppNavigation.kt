package com.example.course2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.course2.navigation.AppNavigation
import com.example.course2.ui.navigation.engeener.engineerNavigation
import com.example.course2.ui.screen.account.accountNavGraph
import com.example.course2.ui.screen.accountant.accountantNavigation
import com.example.course2.ui.screen.loading.loadingScreenNavGraph
import com.example.course2.ui.screen.noRole.noRoleNavGraph
import com.example.course2.ui.screen.user.userNavigation

@Composable
fun AppNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = AppNavigation.startDestination.route
    ) {
        loadingScreenNavGraph()
        accountNavGraph()

        engineerNavigation()
        userNavigation()
        accountantNavigation()
        noRoleNavGraph()

    }

}