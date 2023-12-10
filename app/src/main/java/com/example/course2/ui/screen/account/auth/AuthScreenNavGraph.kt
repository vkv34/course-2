package com.example.course2.ui.screen.account.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.authScreenNavGraph() {
    composable(route = AppNavigation.Account.Auth.route){
        AuthScreen()
    }
}