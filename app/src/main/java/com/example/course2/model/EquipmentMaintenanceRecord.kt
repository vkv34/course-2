package com.example.course2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class EquipmentMaintenanceRecord(
    val id: String = "",
    @Serializable(DateSerializer::class)
    val date: Date = Date(System.currentTimeMillis()),
    val employeeId: Int? = 0,
    val equipmentId: String? = null,
    val condition: Int? = 0,
    val comment: String? = "",

    @SerialName("conditionNavigation")
    val equipmentCondition: EquipmentCondition? = null
)
