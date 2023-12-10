package com.example.course2.model

import kotlinx.serialization.Serializable
import java.util.*
import java.util.Date

@Serializable
data class EquipmentLocationChange(
    val id: Int = 0,
    val equipmentId: String? = null,
    val employeeId: Int? = 0,
    val locationId: Int? = 0,
    val date: String? = null
)
