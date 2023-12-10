package com.example.course2.model

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentCondition(
    val id: Int = 0,
    val name: String = ""
)
