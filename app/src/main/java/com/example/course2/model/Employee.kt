package com.example.course2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    @SerialName("id") val id: Int = 0,
    val login: String = "",
    val password: String = "",
    val lastName: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val email: String = "",
    val roleId: Int = 0,
    val role: Role? = null
)