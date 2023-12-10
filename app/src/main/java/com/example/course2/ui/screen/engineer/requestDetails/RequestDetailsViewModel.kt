package com.example.course2.ui.screen.engineer.requestDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.course2.api.ApiClient
import com.example.course2.api.ApiRoutes
import com.example.course2.model.EquipmentMaintenanceRecord
import com.example.course2.model.ServiceRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RequestDetailsViewModel : ViewModel() {

    private val _equipmentRequest = MutableStateFlow(ServiceRequest())
    val equipmentRequest = _equipmentRequest.asStateFlow()

    private val _maintenanceRecords = MutableStateFlow(listOf<EquipmentMaintenanceRecord>())
    val maintenanceRecords = _maintenanceRecords.asStateFlow()


    private val _uiState = MutableStateFlow(UiState())
    internal val uiState = _uiState.asStateFlow()

    private var requestId: Int = 0


    fun setId(id: Int) {
        requestId = id
        viewModelScope.launch {
            delay(3000)
            val response = ApiClient.apiClient.get(ApiRoutes.ServiceRequest.getById(id))
            if (response.status.isSuccess()) {
                _uiState.update { it.copy(isRequestLoading = false) }
            } else {
                _uiState.update { it.copy(error = response.body()) }
            }
            _equipmentRequest.update { response.body() }

            updateStatuses(id)
            checkInProgress(id)
        }
    }

    private suspend fun updateStatuses(id: Int) {
        _uiState.update { it.copy(isRecordsLoading = true) }
        val recordsResponse = ApiClient.apiClient.get(ApiRoutes.MaintenanceRecord.getById(id))
        if (recordsResponse.status.isSuccess()) {
            _uiState.update { it.copy(isRecordsLoading = false) }
            _maintenanceRecords.update {
                recordsResponse.body<List<EquipmentMaintenanceRecord>>().sortedBy { it.date }
            }
        } else {
            _uiState.update { it.copy(error = recordsResponse.body()) }
        }
    }


    private suspend fun checkInProgress(id: Int) {
        _uiState.update { it.copy(isInProgress = null) }
        val response = ApiClient.apiClient.get(ApiRoutes.ServiceRequest.isInProgress(id))
        if (response.status.isSuccess()) {
            _uiState.update { it.copy(isInProgress = response.body()) }
        } else {
            _uiState.update { it.copy(error = response.body()) }
        }
    }

    fun setInProgress() {
        _uiState.update { it.copy(bottomButtonLoading = true) }
        viewModelScope.launch {
            delay(1000)
            val response =
                ApiClient.apiClient.post(ApiRoutes.ServiceRequest.setInProgress(requestId))
            if (response.status.isSuccess()) {
                _uiState.update { it.copy(error = "Заявка принята") }
            } else {
                _uiState.update { it.copy(error = response.body()) }
            }
            checkInProgress(requestId)
            _uiState.update { it.copy(bottomButtonLoading = false) }

        }
    }

    fun changeDialogState() {
        _uiState.update { it.copy(changeDialogOpened = !it.changeDialogOpened) }
        viewModelScope.launch {
            if (!_uiState.value.changeDialogOpened) {
                checkInProgress(requestId)
                updateStatuses(requestId)
            }
        }
    }


}

internal data class UiState(
    val isRequestLoading: Boolean = true,
    val isRecordsLoading: Boolean = true,
    val error: String = "",
    val isInProgress: Boolean? = null,
    val bottomButtonLoading: Boolean = false,
    val changeDialogOpened: Boolean = false
)