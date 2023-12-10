package com.example.course2.navigation

import androidx.navigation.NavHostController
import com.example.course2.model.Role

fun NavHostController.navigateByRole(
    roleName: String,

    ) {
    when (roleName) {
        Role.enginnerRole -> {
            navigate(AppNavigation.Engeener.route) {
                popUpTo(0)
            }
        }

        Role.equipmentUser -> {
            navigate(AppNavigation.User.route) {
                popUpTo(0)
            }
        }

        Role.accountantRole -> {
            navigate(AppNavigation.Accountant.route) {
                popUpTo(0)
            }
        }


        else -> navigate(AppNavigation.NoRole.route) {
            popUpTo(0)
        }
    }
}

