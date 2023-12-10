package com.example.course2.model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ServiceRequest(
    val id: Int = 0,
    @Serializable(DateSerializer::class)
    val date: Date = Date(System.currentTimeMillis()),
    val name: String = "",
    val reason: String = "",
    val employeeId: Int = 0,
    val equipmentId: String = "",

    val equipment: Equipment? = null
)

