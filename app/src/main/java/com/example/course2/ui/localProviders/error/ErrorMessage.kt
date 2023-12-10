package com.example.course2.ui.localProviders.error

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ErrorMessage(
    val title: String? = null,
    val message: String? = null,
    val show: Boolean = false
)

class ErrorMessageState() {
    private val _state = MutableStateFlow(ErrorMessage())
    val state = _state.asStateFlow()

    fun update(updateScope: (ErrorMessage) -> ErrorMessage) {
        _state.update(updateScope)
    }

    private var job: Job? = null

    suspend fun show(message: String) {
        job?.cancel()
        coroutineScope {
            job = launch {
                _state.update { it.copy(show = true, message = message) }
                delay(10000)
                _state.update { it.copy(show = false, message = "") }
            }
        }
    }


}
