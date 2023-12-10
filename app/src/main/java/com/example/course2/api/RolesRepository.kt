package com.example.course2.api

import com.example.course2.model.Employee
import com.example.course2.model.Role
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class RolesRepository(private val httpClient: HttpClient) {
    suspend fun getCurrentRole(): ApiResult<Role> {
        return try {
            val response = httpClient.get(ApiRoutes.Account.Current.route)
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body<Employee>().role ?: Role())
            } else if (response.status == HttpStatusCode.Unauthorized) {
                ApiResult.UnAuth()
            } else {
                ApiResult.Error(Exception(response.status.description + response.body()))
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }

    }

}