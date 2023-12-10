package com.example.course2.ui.screen.accountant.equipmentDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.course2.api.apiClient
import com.example.course2.model.Equipment
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
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
    val bottomButtonLoading: Boolean = false,
    val isEdit: Boolean? = false
)

class EquipmentDetailsScreenViewModel : ViewModel() {
    val equipment = MutableStateFlow(Equipment(inventoryNumber = "      "))
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
                equipment.update { it.copy(inventoryNumber = "") }

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

    fun edit() {
        uiState.update { it.copy(isEdit = true) }
    }

    fun confirmEdit() {
        uiState.update { it.copy(isEdit = null) }
        viewModelScope.launch {
            apiClient.put("equipment/$id") {
                contentType(ContentType.Application.Json)
                setBody(equipment.value)
            }
            delay(1500)
            uiState.update { it.copy(isEdit = false) }
            uiState.update { it.copy(isLoading = true) }
            delay(1000)
            fetchEquipment(id)
            uiState.update { it.copy(isLoading = false) }
        }

    }


    fun writeOff(
        onDelete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            uiState.update { it.copy(bottomButtonLoading = true) }

            try {
                val response = apiClient.delete("equipment/$id")

                if (response.status.isSuccess()) {
                    uiState.update { it.copy(error = "Успешно удалено") }
                    onDelete()
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