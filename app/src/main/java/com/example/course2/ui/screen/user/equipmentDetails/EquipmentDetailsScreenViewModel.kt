package com.example.course2.ui.screen.user.equipmentDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.course2.api.apiClient
import com.example.course2.model.Equipment
import com.example.course2.model.EquipmentUsage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val error: String = "",
    val isLoading: Boolean = true,
    val isInUsing: Boolean? = null,
    val bottomButtonLoading: Boolean = false
) {
}


class EquipmentDetailsScreenViewModel : ViewModel() {
    val equipment = MutableStateFlow(Equipment())
    val uiState = MutableStateFlow(UiState())

    var id = ""
        set(value) {
            field = value
            viewModelScope.launch {
                uiState.update { it.copy(isLoading = true) }
                delay(2000)
                fetchEquipment(value)
                uiState.update { it.copy(isLoading = false) }

            }
        }




    private suspend fun fetchEquipment(id: String) {
        try {
            val response = apiClient.get("equipment/$id")

            if (response.status.isSuccess()) {
                equipment.update { response.body() }
                val response = apiClient.get("EquipmentCondition/${equipment.value.conditionId}")
                equipment.update { it.copy(condition = response.body()) }
            } else {
                uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
            }

        } catch (e: Exception) {
            uiState.update { it.copy(error = e.localizedMessage ?: "Ошибка") }
        }
        checkInUsing(id)
    }

    private suspend fun checkInUsing(id: String) {
        try {
            val response = apiClient.get("equipment/inUsing/$id")

            if (response.status.isSuccess()) {
                uiState.update { it.copy(isInUsing = response.body()) }
            } else {
                uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
            }

        } catch (e: Exception) {
            uiState.update { it.copy(error = e.localizedMessage ?: "Ошибка") }
        }
    }

    fun use() {
        viewModelScope.launch {
            uiState.update { it.copy(bottomButtonLoading = true) }

            try {
                val response = apiClient.post("equipmentUsage") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        EquipmentUsage(
                            equipmentId = id
                        )
                    )
                }

                if (response.status.isSuccess()) {
                    uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
                } else {
                    uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
                }

            } catch (e: Exception) {
                uiState.update { it.copy(error = e.localizedMessage ?: "Ошибка") }
            } finally {
                uiState.update { it.copy(bottomButtonLoading = false) }
            }
            checkInUsing(id)
        }
    }

    fun unUse() {
        viewModelScope.launch {
            uiState.update { it.copy(bottomButtonLoading = true) }

            try {
                val response = apiClient.post("equipmentUsage") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        EquipmentUsage(
                            equipmentId = id,
                            eventTypeId = 1
                        )
                    )
                }

                if (response.status.isSuccess()) {
                    uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
                } else {
                    uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
                }

            } catch (e: Exception) {
                uiState.update { it.copy(error = e.localizedMessage ?: "Ошибка") }
            } finally {
                uiState.update { it.copy(bottomButtonLoading = false) }
            }
            checkInUsing(id)
        }
    }


}