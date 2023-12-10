package com.example.course2.ui.screen.noRole

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.noRoleNavGraph() {
    composable(AppNavigation.NoRole.route) {
        NoRoleScreen()
    }

}