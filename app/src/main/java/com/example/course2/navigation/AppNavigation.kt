package com.example.course2.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

const val ID_KEY = "ID"


sealed class AppNavigation(
    val title: String,
    val route: String = title
) {

    data object Loading : AppNavigation("Загрузка")

    data object Account : AppNavigation("Аккаунт") {
        data object Auth : AppNavigation("Авторизация")
        data object SignUp : AppNavigation("Регистрация")

        val startDestination = Auth
    }

    data object Engeener : AppNavigation(title = "Инженер") {
        data object HomeScreen : AppNavigation(title = "Главный экран")
        data object MyRequestsScreen : AppNavigation(title = "Мои заявки")
        data object RequestDetailsScreen :
            AppNavigation(title = "Детали Заявки", route = "RequestDetails/{$ID_KEY}") {


            val arguments = listOf(
                navArgument(ID_KEY) {
                    type = NavType.IntType
                }
            )

            fun putId(id: Int): String = "RequestDetails/$id"


        }

    }

    data object User : AppNavigation(title = "Пользователь оборудованием") {
        data object EquipmentList : AppNavigation(title = "Список оборудования")
        data object EquipmentDetails : AppNavigation(title = "Детали оборудования/{$ID_KEY}") {
            val arguments = listOf(
                navArgument(ID_KEY) {
                    type = NavType.StringType
                }
            )

            fun putId(id: String): String = "Детали оборудования/$id"

        }
    }

    data object Accountant : AppNavigation(title = "Бухгалтер") {
        data object EquipmentList : AppNavigation(title = "Список оборудования Бухгалтера")
        data object EquipmentDetails :
            AppNavigation(title = "Детали оборудования Бухгалтера/{$ID_KEY}") {
            val arguments = listOf(
                navArgument(ID_KEY) {
                    type = NavType.StringType
                }
            )

            fun putId(id: String): String = "Детали оборудования Бухгалтера/$id"

        }

        data object Statistic: AppNavigation(title = "Статистика")
    }


    data object NoRole : AppNavigation(title = "No Role")

    companion object {
        val startDestination = Loading
    }
}
