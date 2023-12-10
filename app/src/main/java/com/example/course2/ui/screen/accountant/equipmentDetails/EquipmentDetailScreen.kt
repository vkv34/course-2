package com.example.course2.ui.screen.accountant.equipmentDetails

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.course2.api.ApiClient
import com.example.course2.api.DEFAULT_URL
import com.example.course2.api.apiClient
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.composables.equipment.ExtendedEquipmentLargeCard
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.modifiers.shimmer
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccountantEquipmentDetailsScreen(
    id: String
) {
    val viewModel: EquipmentDetailsScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val equipment by viewModel.equipment.collectAsState()
    val context = LocalContext.current
    val errorController = LocalErrorProvider.current
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        if (uiState.error.isNotEmpty()) {
            scope.launch {
                errorController.show(uiState.error)
            }
        }
    }
    LaunchedEffect(equipment.inventoryNumber) {
        if (equipment.inventoryNumber.isEmpty()) {
            scope.launch {
                errorController.show("Оборудование не найдено")
            }
            navController.popBackStack()
        }
    }


    LaunchedEffect(Unit) {
        viewModel.id = id
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
//                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(text = "Детали оборудования") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            when (uiState.isEdit) {
                                true -> {
                                    viewModel.confirmEdit()
                                }

                                false -> {
                                    viewModel.edit()
                                }

                                else -> Unit
                            }
                        },
                        enabled = uiState.isEdit != null
                    ) {
                        Crossfade(targetState = uiState.isEdit, label = "") {
                            when (it) {
                                true -> {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }

                                false -> {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }

                                else -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(23.dp),
                                        strokeCap = StrokeCap.Round,
                                        strokeWidth = 3.dp
                                    )
                                }
                            }
                        }
                    }
                }

            )
            Crossfade(targetState = uiState.isEdit, label = "") { isEdit ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

//                    var opened by remember {
//                        mutableStateOf(false)
//                    }
                    if (isEdit == false) {
                        ExtendedEquipmentLargeCard(
                            equipment = equipment,
                            isLoading = uiState.isLoading
                        )
//                        DropdownMenu(
//                            expanded = opened,
//                            onDismissRequest = { opened = false },
//
//                            ) {
//                            DropdownMenuItem(text = { Text(text = "Экспорт") }, onClick = {
//
//
//
////                                context.startActivity(
////                                    Intent(
////                                        Intent.ACTION_VIEW,
////                                        Uri.parse(DEFAULT_URL + "api/Equipment/barcode/$id")
////                                    )
////                                )
//                            })
//                        }
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .addHeader("Authorization", ApiClient.token)
                                .data(DEFAULT_URL + "api/Equipment/barcode/$id")
                                .build(),
                            contentDescription = null,
                            modifier =
                            Modifier
                                .height(250.dp)
                                .width(250.dp)
                                .shimmer(
                                    enabled = uiState.isLoading,
                                    cornerRadius = CornerRadius(50f)
                                )
                                .clickable(
                                    enabled = !uiState.isLoading,
                                    onClick = {
//                                        opened = true
                                        val file = File(context.cacheDir, "barcode-$id.png")

                                        scope.launch {
                                            apiClient
                                                .prepareGet(DEFAULT_URL + "api/Equipment/barcode/$id")
                                                .execute { response ->
                                                    val channel = response.bodyAsChannel()

                                                    while (!channel.isClosedForRead) {
                                                        val packet =
                                                            channel.readRemaining(
                                                                DEFAULT_BUFFER_SIZE.toLong()
                                                            )
                                                        while (!packet.isEmpty) {
                                                            val bytes = packet.readBytes()
                                                            file.appendBytes(bytes)
//                                                    println("Received ${file.length()} bytes from ${response.contentLength()}")
                                                        }
                                                    }
//                                            println("A file saved to ${file.path}")

                                                    context.startActivity(
                                                        Intent(
                                                            Intent.ACTION_VIEW,
                                                            FileProvider.getUriForFile(
                                                                context,
                                                                "application",
                                                                file
                                                            )
                                                        ).apply {
                                                            flags =
                                                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                        }
                                                    )
                                                }
                                        }
                                    }
                                )
                        )


                    } else {
                        val enabled = uiState.isEdit == true
                        val editableEquipment = viewModel.equipment

                        OutlinedTextField(
                            value = equipment.equipmentName,
                            onValueChange = { str ->
                                editableEquipment.update { it.copy(equipmentName = str) }
                            },
                            isError = equipment.equipmentName.isBlank(),
                            supportingText = {
                                if (equipment.equipmentName.isBlank()) {
                                    Text(text = "Наименование не может быть пустым")
                                }
                            },
                            label = { Text(text = "Наименование") },
                            enabled = enabled,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

//                    DropDownMenu(
//                        options = items,
//                        selectedOption = selectedItem,
//                        onSelectionChanged = { selectedItem = it },
//                        displayValue = { it?.name ?: "" },
//                        title = "Модель",
//                        editable = true,
//                        onTextChange = {
//                            selectedItem = selectedItem?.copy(id = 0, name = it) ?: Item(name = it)
//                        }
//
//                    )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = equipment.purchaseCost.toString(),
                            onValueChange = { str ->
                                try {
                                    editableEquipment.update { it.copy(purchaseCost = str.toDouble()) }
                                } catch (_: Exception) {
                                }

                            },
                            label = {
                                Text(text = "Стоимость закупки")
                            },
                            isError = equipment.purchaseCost < 0,
                            enabled = enabled,
                            supportingText = {
                                if (equipment.purchaseCost < 0) {
                                    Text(text = "Цена закупки не может быть меньше 0")
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()

                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = equipment.marketValue.toString(),
                            onValueChange = { str ->
                                try {
                                    editableEquipment.update { it.copy(marketValue = str.toDouble()) }

                                } catch (_: Exception) {
                                }

                            },
                            supportingText = {
                                if (equipment.marketValue < 0) {
                                    Text(text = "Рыночная цена не может быть меньше 0")
                                }
                            },
                            enabled = enabled,
                            label = {
                                Text(text = "Рыночная стоимость")
                            },
                            isError = equipment.marketValue < 0,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()

                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val dateFormatter = remember {
                            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        }
                        var dateOpened by remember {
                            mutableStateOf(false)
                        }
                        val datePickerState =
                            rememberDatePickerState(
                                initialSelectedDateMillis = System.currentTimeMillis(),
                                yearRange = 2023..2100
                            )

                        LaunchedEffect(datePickerState.selectedDateMillis) {
                            editableEquipment.update {
                                it.copy(
                                    shelfLife = Date(
                                        datePickerState.selectedDateMillis
                                            ?: (System.currentTimeMillis() + 86000)
                                    )
                                )
                            }
                        }

                        if (dateOpened) {
                            DatePickerDialog(
                                onDismissRequest = { dateOpened = false },
                                confirmButton = {
                                    Button(onClick = { dateOpened = false }) {
                                        Text(text = "Закрыть")
                                    }
                                }
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    dateValidator = {
                                        System.currentTimeMillis() - it < 80000
                                    }
                                )
                            }
                        }

                        Row(
                            Modifier.clickable(enabled) {
                                dateOpened = true
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Срок годности")
                            TextButton(
                                onClick = {
                                    dateOpened = true
                                },
                                enabled = enabled,
                            ) {
                                Text(
                                    text = dateFormatter.format(
                                        Date(
                                            datePickerState.selectedDateMillis
                                                ?: System.currentTimeMillis()
                                        )
                                    )
                                )
                            }
                        }


                    }
                }
            }

        }

        OutlinedButton(
            enabled = !uiState.bottomButtonLoading,
            onClick = {
                viewModel.writeOff {
                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
//                .sizeIn(minHeight = 50.dp, minWidth = 500.dp )
                .imePadding()
                .padding(32.dp),
        ) {
            AnimatedVisibility(visible = uiState.bottomButtonLoading) {
                Row {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Text(text = "Списать оборудование")

        }
    }
}