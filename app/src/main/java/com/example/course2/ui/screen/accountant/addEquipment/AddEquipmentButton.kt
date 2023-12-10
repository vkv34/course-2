package com.example.course2.ui.screen.accountant.addEquipment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.course2.api.apiClient
import com.example.course2.model.Equipment
import com.example.course2.model.Item
import com.example.course2.ui.composables.dropdown.DropDownMenu
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEquipmentButton(
    modifier: Modifier = Modifier,
    onClose: (id: String?) -> Unit = {}
) {

    var itemText by remember {
        mutableStateOf("")
    }
    var items by remember {
        mutableStateOf(listOf<Item>())
    }
    var selectedItem: Item? by remember {
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()

    var opened by remember {
        mutableStateOf(false)
    }

    var adding by remember {
        mutableStateOf(false)
    }

    var name by remember {
        mutableStateOf("")
    }
    var purchaseCost: Double by remember {
        mutableDoubleStateOf(0.0)
    }
    var marketCost: Double by remember {
        mutableDoubleStateOf(0.0)
    }
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

    LaunchedEffect(Unit) {
        scope.launch {
            items = apiClient.get("item").body()
            selectedItem = items.firstOrNull()
        }
    }

    FloatingActionButton(
        modifier = modifier,
        onClick = {
            opened = true
        }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = Icons.Default.Add.name
        )
    }

    if (opened) {
        ModalBottomSheet(
            onDismissRequest = { opened = false },
            windowInsets = BottomSheetDefaults.windowInsets
                .exclude(WindowInsets.displayCutout)
                .exclude(WindowInsets.navigationBars)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    isError = name.isBlank(),
                    supportingText = {
                        if (name.isBlank()) {
                            Text(text = "Наименование не может быть пустым")
                        }
                    },
                    label = { Text(text = "Наименование") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                DropDownMenu(
                    options = items,
                    selectedOption = selectedItem,
                    onSelectionChanged = { selectedItem = it },
                    displayValue = { it?.name ?: "" },
                    title = "Модель",
                    editable = true,
                    onTextChange = {
                        selectedItem = selectedItem?.copy(id = 0, name = it) ?: Item(name = it)
                    }

                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = purchaseCost.toString(),
                    onValueChange = {
                        try {
                            purchaseCost = it.toDouble()
                        } catch (_: Exception) {
                        }

                    },
                    label = {
                        Text(text = "Стоимость закупки")
                    },
                    isError = purchaseCost < 0,
                    supportingText = {
                        if (purchaseCost < 0) {
                            Text(text = "Цена закупки не может быть меньше 0")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()

                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = marketCost.toString(),
                    onValueChange = {
                        try {
                            marketCost = it.toDouble()
                        } catch (_: Exception) {
                        }

                    },
                    supportingText = {
                        if (marketCost < 0) {
                            Text(text = "Рыночная цена не может быть меньше 0")
                        }
                    },
                    label = {
                        Text(text = "Рыночная стоимость")
                    },
                    isError = marketCost < 0,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()

                )
                Spacer(modifier = Modifier.height(16.dp))



                Row(
                    Modifier.clickable {
                        dateOpened = true
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Срок годности")
                    TextButton(onClick = {
                        dateOpened = true
                    }) {
                        Text(
                            text = dateFormatter.format(
                                Date(
                                    datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                                )
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            adding = true
                            delay(1000)
                            val item = if (selectedItem?.id == 0) {
                                apiClient.post("item").body<Item>()
                            } else {
                                selectedItem ?: error("Item not selected")
                            }
                            val response = apiClient.post("equipment") {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    Equipment(
                                        equipmentName = name,
                                        itemId = item.id,
                                        purchaseCost = purchaseCost,
                                        marketValue = marketCost,
                                        shelfLife = Date(
                                            datePickerState.selectedDateMillis
                                                ?: (System.currentTimeMillis() + 86000)
                                        )
                                    )
                                )
                            }

                            if (response.status.isSuccess()) {
                                val equipment = response.body<Equipment>()
                                onClose(equipment.inventoryNumber)
                            }
                            adding = false
                            opened = false
                        }
                    },
                    enabled = !adding &&
                            (selectedItem?.name?.isNotEmpty() == true) &&
                            (name.isNotEmpty()) &&
                            (purchaseCost >= 0) &&
                            (marketCost >= 0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    AnimatedVisibility(visible = adding) {
                        if (adding) {
                            Row {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeCap = StrokeCap.Round,
                                    strokeWidth = 4.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                    Text(text = "Добавить оборудование")
                }
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
    }
}