package com.example.course2.ui.screen.accountant.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation

fun NavGraphBuilder.mainEquipmentNavGraph(){
    composable(AppNavigation.Accountant.EquipmentList.route) {
        MainEquipmentList()
    }
}
