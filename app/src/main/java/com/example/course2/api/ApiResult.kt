package com.example.course2.api

import androidx.compose.runtime.Stable

sealed class ApiResult<T>(){
    @Stable
    class UnAuth<T>() : ApiResult<T>()
    data class Success<T>(val data: T): ApiResult<T>()
    data class Error<T>(val error: Throwable): ApiResult<T>()
}

