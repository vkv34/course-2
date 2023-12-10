package com.example.course2.model

import java.util.*
import java.util.Date
import java.math.BigDecimal

data class EquipmentPurchaseContract(
    val id: Int = 0,
    val contractNumber: Int? = 0,
    val name: String? = "",
    val supplierId: Int? = 0,
    val employeeId: Int? = 0,
    val date: Date? = null,
    val amount: BigDecimal? = BigDecimal(0)
)
