package com.example.course2.ui.screen.user.equipmentDetails.request

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.course2.api.apiClient
import com.example.course2.model.Equipment
import com.example.course2.model.EquipmentLocationChange
import com.example.course2.model.Location
import com.example.course2.model.ServiceRequest
import com.example.course2.ui.composables.dropdown.DropDownMenu
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeRequestScreen(
    id: String,
    onClose: () -> Unit
) {

    val scope = rememberCoroutineScope()

    var dialogOpened by remember {
        mutableStateOf(false)
    }

    var theme by remember {
        mutableStateOf("")
    }
    var text by remember {
        mutableStateOf("")
    }

    var locations by remember {
        mutableStateOf(listOf<Location>())
    }

    var selectedLocation: Location? by remember {
        mutableStateOf(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        locations = apiClient.get("location").body()
    }

    TextButton(onClick = { dialogOpened = true }) {
        Text(text = "Сообщить о неисправности")
    }

    if (dialogOpened) {
        ModalBottomSheet(
            onDismissRequest = {
                dialogOpened = false
                onClose()
            },
            windowInsets = BottomSheetDefaults.windowInsets
                .exclude(WindowInsets.displayCutout)
                .exclude(WindowInsets.navigationBars),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(value = theme,
                    onValueChange = {
                        theme = it
                    },
                    label = {
                        Text(text = "Тема обращения")
                    }
                )
                Spacer(Modifier.height(8.dp))
                DropDownMenu(
                    options = locations,
                    selectedOption = selectedLocation,
                    onSelectionChanged = {
                        selectedLocation = it
                    },
                    displayValue = {
                        it?.name ?: ""
                    },
                    title = "Расположение"
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    label = {
                        Text(text = "Комментарий")
                    },
                    modifier = Modifier.heightIn(min = 250.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    enabled = !isLoading,
                    onClick = {
                        isLoading = true
                        scope.launch {
                            apiClient.post("ServiceRequest") {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    ServiceRequest(
                                        equipmentId = id,
                                        reason = text,
                                        name = theme
                                    )
                                )
                            }
                            val equipment = apiClient.get("equipment/$id").body<Equipment>()
                            apiClient.put("equipment/$id") {
                                contentType(ContentType.Application.Json)
                                setBody(equipment.copy(locationId = selectedLocation?.id ?: 0))
                            }
                            apiClient.post("equipmentLocationChange") {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    EquipmentLocationChange(
                                        equipmentId = id,
                                        locationId = selectedLocation?.id ?: 0
                                    )
                                )
                            }
                            isLoading = false
                            onClose()
                            dialogOpened = false
                        }
                    }) {
                    Text(text = "Отправить обращение")
                }

            }
        }
    }
}