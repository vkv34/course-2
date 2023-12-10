package com.example.course2.ui.screen.engineer.requestDetails

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation
import com.example.course2.navigation.ID_KEY

fun NavGraphBuilder.engineenerRequestDetailsNavGraph() {
    composable(
        AppNavigation.Engeener.RequestDetailsScreen.route,
        arguments = AppNavigation.Engeener.RequestDetailsScreen.arguments,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                initialOffset = { it / 2 },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn()

        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis = 300)

            ) + fadeOut()
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = 300)

            ) + fadeOut() },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
        }
    ) {
        RequestDetailsScreen(
            requestId = it.arguments?.getInt(ID_KEY) ?: 1
        )
    }
}