package com.example.course2.ui.screen.user.equipmentList

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.mainEquipmentNavGraph(){
    composable(AppNavigation.User.EquipmentList.route) {
        MainEquipmentList()
    }
}
