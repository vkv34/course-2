package com.example.course2.ui.localProviders.error

import androidx.compose.runtime.compositionLocalOf

val LocalErrorProvider = compositionLocalOf<ErrorMessageState> { error("Error Provider is not provided") }