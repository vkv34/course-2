package com.example.course2.model

import kotlinx.serialization.SerialInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.io.Serial
import java.util.*
import java.util.Date

@Serializable
data class EquipmentUsage(
    val id: Int = 0,
    val employeeId: Int? = 0,
    val equipmentId: String? = null,
    @Serializable(DateSerializer::class)
    val date: Date? = null,
    val duration: Int? = 0,
    val eventTypeId: Int? = 0
)
