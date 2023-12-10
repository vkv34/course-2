package com.example.course2.ui.screen.accountant.equipmentDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation
import com.example.course2.navigation.ID_KEY

fun NavGraphBuilder.accountantEquipmentDetailsNavGraph(){
    composable(
        AppNavigation.Accountant.EquipmentDetails.route,
        arguments = AppNavigation.Accountant.EquipmentDetails.arguments
    ){
        val id = it.arguments?.getString(ID_KEY)?:""
        AccountantEquipmentDetailsScreen(id = id)
    }
}