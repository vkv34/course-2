package com.example.course2.ui.screen.engineer.requestDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.composables.matianceRecord.EquipmentMaintenanceRecordList
import com.example.course2.ui.composables.serviceRequest.ServiceRequestCard
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.modifiers.shimmer
import com.example.course2.ui.screen.engineer.requestDetails.changeState.ChangeStateScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(requestId: Int = 1) {


    val viewModel: RequestDetailsViewModel = viewModel()
    val request by viewModel.equipmentRequest.collectAsState()
    val records by viewModel.maintenanceRecords.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val errorProvider = LocalErrorProvider.current
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    LaunchedEffect(requestId) {
        viewModel.setId(requestId)
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            scope.launch {
                errorProvider.show(uiState.error)
            }
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)

        ) {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = Icons.Default.ArrowBack.name
                        )
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .alignByBaseline(),
                            text = "Заявка №"
                        )
                        Text(
                            modifier = Modifier
                                .shimmer(uiState.isRequestLoading, cornerRadius = CornerRadius(25f))
                                .then(
                                    if (uiState.isRequestLoading) {
                                        Modifier
                                            .size(25.dp)
                                            .alignBy(FirstBaseline)
                                    } else Modifier.alignByBaseline()
                                ),
                            text = if (uiState.isRequestLoading) "" else request.id.toString()
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .animateContentSize()
            ) {
                ServiceRequestCard(serviceRequest = request, isLoading = uiState.isRequestLoading)
                Spacer(modifier = Modifier.height(32.dp))
                EquipmentMaintenanceRecordList(
                    records = records,
                    isLoading = uiState.isRecordsLoading
                )
                Spacer(modifier = Modifier.height(150.dp))
            }
        }

        AnimatedContent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
//                .sizeIn(minHeight = 50.dp, minWidth = 500.dp )
                .imePadding()
                .padding(32.dp),
            targetState = uiState.isInProgress,
            label = "",
            transitionSpec = {
                fadeIn() + slideInVertically(
                    animationSpec = spring(dampingRatio = 0.75f)
                ) { it } togetherWith
                        slideOutVertically { it }
            }
        )
        {
            when (it) {
                true -> {
                    OutlinedButton(
                        onClick = viewModel::changeDialogState,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )


                    ) {
                        Text(text = "Изменить статус")
                    }
                }

                false -> {
                    OutlinedButton(
                        enabled = !uiState.bottomButtonLoading,
                        onClick = viewModel::setInProgress,
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
                        Text(text = "Принять заявку")

                    }
                }

                null -> Unit
            }
        }
    }

    ChangeStateScreen(
        opened = uiState.changeDialogOpened,
        equipmentId = request.equipmentId,
        onClose = viewModel::changeDialogState
    )
}