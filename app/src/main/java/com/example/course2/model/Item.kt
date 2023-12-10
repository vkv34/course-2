package com.example.course2.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int = 0,
    val name: String = ""
)
