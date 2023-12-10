package com.example.course2.ui.screen.user

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.course2.navigation.AppNavigation
import com.example.course2.ui.screen.user.equipmentDetails.equipmentDetailsNavGraph
import com.example.course2.ui.screen.user.equipmentList.mainEquipmentNavGraph

fun NavGraphBuilder.userNavigation() {
    navigation(
        route = AppNavigation.User.route,
        startDestination = AppNavigation.User.EquipmentList.route
    ) {
        mainEquipmentNavGraph()
        equipmentDetailsNavGraph()
    }

}


