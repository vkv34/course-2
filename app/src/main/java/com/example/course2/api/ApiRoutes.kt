package com.example.course2.api

sealed class ApiRoutes(
    val route: String
) {
    data object Account : ApiRoutes("Account/") {
        data object AuthByLoginAndPassword : ApiRoutes(route + "AuthByLoginAndPassword/")
        data object Register : ApiRoutes(route + "Register/")
        data object Current : ApiRoutes(route + "Current/")
    }

    data object ServiceRequest : ApiRoutes("servicerequest/") {
        fun getById(id: Int) = route + id.toString()
        fun isInProgress(id: Int) = route + "isInProgress/" + id.toString()
        fun setInProgress(id: Int) = route + "SetInProgress/" + id.toString()
        data object MyRequestsRoute : ApiRoutes(route+"my/")
        data object AvailableRequestsRoute : ApiRoutes(route+"available/")

    }

    data object Equipment : ApiRoutes("Equipment/") {
        fun getById(id: String) = route + id
    }

    data object Location : ApiRoutes("Location/") {
        fun getById(id: Int) = route + id.toString()
    }

    data object MaintenanceRecord : ApiRoutes("EquipmentMaintenanceRecord/") {
        fun getById(id: Int) = route + id.toString()
    }
    data object RequestStatuses : ApiRoutes("RequestStatus/") {
        fun getById(id: Int) = route + id.toString()
    }
}