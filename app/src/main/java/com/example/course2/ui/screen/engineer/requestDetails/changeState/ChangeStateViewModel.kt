package com.example.course2.ui.screen.engineer.requestDetails.changeState

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.course2.api.ApiClient
import com.example.course2.api.ApiRoutes
import com.example.course2.model.EquipmentMaintenanceRecord
import com.example.course2.model.RequestStatus
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeStateViewModel : ViewModel() {
    private val _statuses = MutableStateFlow(listOf<RequestStatus>())
    val statuses = _statuses.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    internal val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            updateStatuses()
        }
    }

    var equipmentId = ""


    fun selectStatus(status: RequestStatus) {
        _uiState.update { it.copy(selectedStatus = status) }
    }

    private suspend fun updateStatuses() {
        _uiState.update { it.copy(isLoading = true) }
        delay(3000)
        val response = ApiClient.apiClient.get(ApiRoutes.RequestStatuses.route)

        if (response.status.isSuccess()) {
            _statuses.update { response.body() }
        } else {
            _uiState.update { it.copy(error = response.body()) }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    fun setComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    fun saveState(
        onSuccess: () -> Unit
    ) {
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            delay(1000)
            val response = ApiClient.apiClient.post(ApiRoutes.MaintenanceRecord.route) {
                contentType(ContentType.Application.Json)
                setBody(
                    EquipmentMaintenanceRecord(
                        equipmentId = equipmentId,
                        condition = _uiState.value.selectedStatus?.id
                    )
                )
            }

            if (!response.status.isSuccess()) {
                _uiState.update { it.copy(error = response.body()) }
            }else  {
                _uiState.update { it.copy(error = "Состояние успешно изменено") }
                onSuccess()
            }

            _uiState.update { it.copy(isSaving = false) }

        }
    }


}

internal data class UiState(
    val selectedStatus: RequestStatus? = null,
    val comment: String = "",
    val error: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false
)