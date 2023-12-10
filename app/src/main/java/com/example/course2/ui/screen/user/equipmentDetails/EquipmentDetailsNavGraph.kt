package com.example.course2.ui.screen.user.equipmentDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.course2.navigation.AppNavigation
import com.example.course2.navigation.ID_KEY

fun NavGraphBuilder.equipmentDetailsNavGraph(){
    composable(
        AppNavigation.User.EquipmentDetails.route,
        arguments = AppNavigation.User.EquipmentDetails.arguments
    ){
        val id = it.arguments?.getString(ID_KEY)?:""
        EquipmentDetailsScreen(id = id)
    }
}