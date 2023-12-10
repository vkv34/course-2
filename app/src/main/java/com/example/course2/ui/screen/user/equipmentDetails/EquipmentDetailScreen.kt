package com.example.course2.ui.screen.user.equipmentDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.course2.api.ApiClient
import com.example.course2.api.DEFAULT_URL
import com.example.course2.ui.composables.equipment.EquipmentLargeCard
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.modifiers.shimmer
import com.example.course2.ui.screen.user.equipmentDetails.request.ComposeRequestScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentDetailsScreen(
    id: String
) {
    val viewModel: EquipmentDetailsScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val equipment by viewModel.equipment.collectAsState()
    val context = LocalContext.current
    val errorController = LocalErrorProvider.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        if (uiState.error.isNotEmpty()) {
            scope.launch {
                errorController.show(uiState.error)
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.id = id
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(title = { Text(text = "Детали оборудования") })
            EquipmentLargeCard(equipment = equipment, isLoading = uiState.isLoading)

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
                    .shimmer(enabled = uiState.isLoading, cornerRadius = CornerRadius(50f))
            )

            ComposeRequestScreen(id = id) {

            }
        }

        Crossfade(
            targetState = uiState.isInUsing,
            modifier = Modifier
                .align(Alignment.BottomCenter)
//                .sizeIn(minHeight = 50.dp, minWidth = 500.dp )
                .imePadding()
                .padding(32.dp),
            label = ""
        ) { isInUsing ->
            when (isInUsing) {
                true -> {
                    OutlinedButton(
                        enabled = !uiState.bottomButtonLoading,
                        onClick = viewModel::unUse,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
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
                        Text(text = "Сдать оборудование")

                    }
                }

                false -> {
                    OutlinedButton(
                        enabled = !uiState.bottomButtonLoading,
                        onClick = viewModel::use,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
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
                        Text(text = "Использовать оборудование")

                    }
                }

                null -> Unit
            }
        }
    }
}