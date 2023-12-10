package com.example.course2.ui.screen.accountant

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.course2.navigation.AppNavigation
import com.example.course2.ui.screen.accountant.equipmentDetails.accountantEquipmentDetailsNavGraph
import com.example.course2.ui.screen.accountant.home.mainEquipmentNavGraph
import com.example.course2.ui.screen.accountant.statistic.statisticScreenGraph

fun NavGraphBuilder.accountantNavigation() {
    navigation(
        route = AppNavigation.Accountant.route,
        startDestination = AppNavigation.Accountant.EquipmentList.route
    ) {
        mainEquipmentNavGraph()
        accountantEquipmentDetailsNavGraph()
        statisticScreenGraph()
    }

}


