package com.example.course2.model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Equipment(
    val inventoryNumber: String = "",
    val equipmentName: String = "",
    val purchaseCost: Double = 0.0,
    val marketValue: Double = 0.0,
    @Serializable(DateSerializer::class)
    val purchaseDate: Date = Date(0),
    @Serializable(DateSerializer::class)
    val shelfLife: Date? = Date(0),
    val conditionId: Int = Int.MIN_VALUE,
    val locationId: Int = Int.MIN_VALUE,
    val itemId: Int = Int.MIN_VALUE,

    val location: Location? = null,
    val condition: EquipmentCondition? = null
)