package com.example.course2.ui.screen.engineer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.course2.api.ApiClient
import com.example.course2.api.ApiRoutes
import com.example.course2.model.Equipment
import com.example.course2.model.ServiceRequest
import com.example.course2.ui.composables.filter.SearchOptionParameter
import com.example.course2.ui.composables.search.FilterParameter
import com.example.course2.ui.composables.search.SearchParameters
import com.example.course2.ui.composables.search.SearchState
import com.example.course2.ui.composables.search.SortParameter
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserHomeScreenViewModel : ViewModel() {

    private val _requests = MutableStateFlow(listOf<ServiceRequest>())
    val request = _requests.asStateFlow()
    private val _uiState = MutableStateFlow(UserHomeScreenState())
    val uiState = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _displayRequests = MutableStateFlow(listOf<ServiceRequest>())
    val displayRequests = _displayRequests.asStateFlow()

    init {

        viewModelScope.launch {
            fetchRequests()

        }

    }

    suspend fun fetchRequests() {
        val response =
            ApiClient.apiClient.get(ApiRoutes.ServiceRequest.AvailableRequestsRoute.route)
        if (response.status.isSuccess()) {
            _uiState.update { it.copy(error = "") }
            _requests.update {
                response.body()
            }

        } else {
            _uiState.update { it.copy(error = response.status.description) }
        }

        _requests.update {
            it.map { serviceRequest ->
                try {
                    val equipment = ApiClient.apiClient.get(
                        ApiRoutes.Equipment.getById(
                            serviceRequest.equipmentId
                        )
                    ).body<Equipment?>()
                    serviceRequest.copy(
                        equipment = equipment
                    )
                } catch (_: Exception) {
                    serviceRequest
                }
            }
        }
        _displayRequests.update { _requests.value }

        _requests.update {
            it.map { serviceRequest ->
                serviceRequest.copy(
                    equipment = serviceRequest.equipment?.copy(
                        location = ApiClient.apiClient.get(
                            ApiRoutes.Location.getById(
                                serviceRequest.equipment.locationId
                            )
                        ).body()
                    )
                )
            }
        }
        _displayRequests.update { _requests.value }

    }

    fun updateQuery(query: String) {
        _searchState.update {
            it.copy(query = query)
        }

        val sortedRequests = _requests.value
            .filter { serviceRequest ->
                serviceRequest.name.contains(query, ignoreCase = true) ||
                        serviceRequest.equipment?.equipmentName?.contains(
                            query,
                            ignoreCase = true
                        ) == true
            }
            .filter { serviceRequest ->
                with(_searchState.value.parameters) {
                    if (contains(FilterParameter.WithComment)) {
                        serviceRequest.reason.isNotEmpty()
                    } else if (contains(FilterParameter.WithoutComment)) {
                        serviceRequest.reason.isEmpty()
                    } else {
                        true
                    }
                }
            }.apply {
                if (_searchState.value.parameters.contains(SortParameter.Asc)) {
                    sortedBy { it.id }
                } else {
                    sortedByDescending { it.id }
                }
            }

        _displayRequests.update {
            sortedRequests
        }
    }

    fun updateParameters(searchParameters: List<SearchParameters?>) {
        _searchState.update {
            it.copy(parameters = searchParameters)
        }

        val comparator =
            Comparator<ServiceRequest> { o1, o2 ->
                if (searchParameters.contains(SortParameter.Asc)) {
                    o1.id.compareTo(o2.id)
                } else {
                    if (o1.id < o2.id) 1 else -1
                }
            }
        val sortedRequests = _requests.value
            .filter { serviceRequest ->
                serviceRequest.name.contains(_searchState.value.query, ignoreCase = true) ||
                        serviceRequest.equipment?.equipmentName?.contains(
                            _searchState.value.query,
                            ignoreCase = true
                        ) == true
            }
            .filter { serviceRequest ->
                with(searchParameters) {
                    if (contains(FilterParameter.WithComment)) {
                        serviceRequest.reason.isNotEmpty()
                    } else if (contains(FilterParameter.WithoutComment)) {
                        serviceRequest.reason.isEmpty()
                    } else {
                        true
                    }
                }
            }.sortedWith(comparator)

        _displayRequests.update {
            sortedRequests
        }

    }


}




data class UserHomeScreenState(
    val error: String = "",
)