package com.example.course2.ui.screen.accountant.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.course2.navigation.AppNavigation
import com.example.course2.protobuf.authPreferencesStore
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.composables.equipment.EquipmentList
import com.example.course2.ui.composables.filter.SearchOptionButton
import com.example.course2.ui.composables.search.SortParameter
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.screen.accountant.addEquipment.AddEquipmentButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainEquipmentList() {
    val viewModel: MainEquipmentViewModel = viewModel()
    val equipments by viewModel.equipments.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val searchState by viewModel.searchState.collectAsState()
    val errorController = LocalErrorProvider.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val barcodeScannerLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = {
            try {
                navController.navigate(AppNavigation.Accountant.EquipmentDetails.putId(it.contents))
            } catch (e: Exception) {
                scope.launch {
                    errorController.show("Оборудование не найдено")
                }
            }

        }
    )

    LaunchedEffect(Unit) {
        viewModel.reFetch()
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text(text = "Оборудование") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            context.authPreferencesStore.updateData {
                                it.toBuilder().setRole("").setToken("").setIsAuth(false).build()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = Icons.Default.ExitToApp.name
                        )
                    }
                },
                )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = searchState.query,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onValueChange = viewModel::updateQuery,
                    trailingIcon = {
                        IconButton(onClick = {
                            barcodeScannerLauncher.launch(ScanOptions().apply {
                                setPrompt("Отсканируйте QR-код")
                                setBeepEnabled(false)
                            })
                        }) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = Icons.Default.AccountBox.name
                            )
                        }
                    },

                    label = {
                        Text(text = "Поиск")
                    }
                )
                SearchOptionButton(
                    parameters = listOf(
                        SortParameter,
                    ),
                    icon = Icons.Default.Menu,
                    modifier = Modifier.weight(0.2f),
                    onOptionSelectionChanged = viewModel::updateParameters
                )
                IconButton(onClick = {
                    navController.navigate(AppNavigation.Accountant.Statistic.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = Icons.Default.AccountBox.name
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

            }
            EquipmentList(records = equipments, onEquipmentClick = {
                navController.navigate(AppNavigation.Accountant.EquipmentDetails.putId(it.inventoryNumber))
            }, isLoading = uiState.isLoading)
        }

        AddEquipmentButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onClose = {
                if (!it.isNullOrBlank()) {
                    navController.navigate(AppNavigation.Accountant.EquipmentDetails.putId(it))
                }
                viewModel.reFetch()
            }
        )
    }

}