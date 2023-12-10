package com.example.course2.ui.screen.accountant.statistic

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.statisticScreenGraph(){
    composable(AppNavigation.Accountant.Statistic.route){
        StatisticScreen()
    }
}
