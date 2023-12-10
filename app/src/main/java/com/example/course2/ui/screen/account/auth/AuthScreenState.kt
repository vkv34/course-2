package com.example.course2.ui.screen.account.auth

import androidx.compose.runtime.Stable

@Stable
internal data class AuthScreenState(
    val login: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
)