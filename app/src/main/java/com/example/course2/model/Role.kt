package com.example.course2.model

import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val id: Int = 0,
    val name: String = ""
){
    companion object{
        const val equipmentUser = "Пользователь оборудования"
        const val enginnerRole = "Инженер"
        const val accountantRole = "Бухгалтер"
    }
}

