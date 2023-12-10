package com.example.course2.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

private const val TAG = "API"

val apiClient = ApiClient.apiClient
val DEFAULT_URL = "http://192.168.1.31:8888/"

object ApiClient {

    var token = ""

    val apiClient = HttpClient(OkHttp) {

        engine {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }
            )
        }

        defaultRequest {
            url(DEFAULT_URL + "api/")
            bearerAuth(token.ifEmpty { "asd" })
        }

        HttpResponseValidator {
            validateResponse {
                when (it.status) {
                    HttpStatusCode.InternalServerError -> {
                        exceptionHandler("Внутрення ошибка сервера")
                        Log.d(TAG, ": ${it.body<String>()}")
                    }

                    HttpStatusCode.Forbidden -> {
                        exceptionHandler("Недостаточно прав для выполнения действия")
                    }

                    HttpStatusCode.BadRequest -> {
//                        exceptionHandler("Ошибка ${it.body<String>().drop(1).dropLast(1)}")
                    }
                }
            }
        }


    }

    var exceptionHandler: (message: String) -> Unit = {}
}

