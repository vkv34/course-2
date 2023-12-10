package com.example.course2.ui.screen.accountant.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.course2.api.apiClient
import com.example.course2.model.Equipment
import com.example.course2.ui.composables.search.SearchParameters
import com.example.course2.ui.composables.search.SearchState
import com.example.course2.ui.composables.search.SortParameter
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val error: String = "",
    val isLoading: Boolean = true
)

class MainEquipmentViewModel : ViewModel() {
    val equipments = MutableStateFlow(listOf<Equipment>())
    val uiState = MutableStateFlow(UiState())

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    init {
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true) }
            delay(2000)
            fetchEquipments()
            uiState.update { it.copy(isLoading = false) }

        }
    }

    fun reFetch() {
        viewModelScope.launch {
            fetchEquipments()
        }
    }


    private suspend fun fetchEquipments() {
        try {
            val response = apiClient.get("equipment/")

            if (response.status.isSuccess()) {
                equipments.update { response.body() }
            } else {
                uiState.update { it.copy(error = response.bodyAsText().drop(1).dropLast(1)) }
            }

        } catch (e: Exception) {
            uiState.update { it.copy(error = e.localizedMessage ?: "Ошибка") }
        }
    }

    private var searchJob: Job? = null


    fun updateQuery(query: String) {
        _searchState.update {
            it.copy(query = query)
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            uiState.update { it.copy(isLoading = true) }
            equipments.update { listOf() }
            delay(1000)
            fetchEquipments()


            val filteredEquipments = equipments.value
                .filter { equipment ->
                    equipment.equipmentName
                        .plus(equipment.inventoryNumber)
                        .contains(query, ignoreCase = true)
                }
                .apply {
                    if (_searchState.value.parameters.contains(SortParameter.Asc)) {
                        sortedBy { it.equipmentName }
                    } else {
                        sortedByDescending { it.equipmentName }
                    }
                }

            equipments.update {
                filteredEquipments
            }
        }
    }

    fun updateParameters(searchParameters: List<SearchParameters?>) {
        _searchState.update {
            it.copy(parameters = searchParameters)
        }

        val comparator =
            Comparator<Equipment> { o1, o2 ->
                if (searchParameters.contains(SortParameter.Asc)) {
                    o1.inventoryNumber.compareTo(o2.inventoryNumber)
                } else {
                    if (o1.inventoryNumber < o2.inventoryNumber) 1 else -1
                }
            }
        val filteredEquipments = equipments.value
            .filter { equipment ->
                equipment.equipmentName
                    .plus(equipment.inventoryNumber)
                    .contains(searchState.value.query, ignoreCase = true)
            }
            .sortedWith(comparator)

        equipments.update {
            filteredEquipments
        }

    }
}