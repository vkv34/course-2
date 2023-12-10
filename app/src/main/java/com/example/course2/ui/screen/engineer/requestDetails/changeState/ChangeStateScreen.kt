package com.example.course2.ui.screen.engineer.requestDetails.changeState

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.modifiers.shimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeStateScreen(
    opened: Boolean,
    equipmentId: String,
    onClose: () -> Unit
) {
    val viewModel: ChangeStateViewModel = viewModel()
    val statuses by viewModel.statuses.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val errorController = LocalErrorProvider.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(equipmentId) {
        viewModel.equipmentId = equipmentId
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            scope.launch {
                errorController.show(uiState.error)
            }
        }
    }


    if (opened) {
        ModalBottomSheet(
            onDismissRequest = onClose,
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
                statuses.forEachIndexed { index, requestStatus ->
                    key(index) {
                        Divider()
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectStatus(requestStatus) },
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = SpaceBetween
                        ) {
                            RadioButton(
                                modifier = Modifier.weight(0.2f),
                                selected = requestStatus == uiState.selectedStatus,
                                onClick = { viewModel.selectStatus(requestStatus) }
                            )

                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .then(
                                        if (uiState.isLoading) {
                                            Modifier
                                                .size(width = 250.dp, height = 25.dp)
                                                .shimmer(true, cornerRadius = CornerRadius(25f))
                                        } else Modifier
                                    ),
                                text = requestStatus.name
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.comment,
                    onValueChange = viewModel::setComment,
                    label = {
                        Text(text = "Комментарий")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { viewModel.saveState(onClose) },
                    enabled = uiState.selectedStatus != null && !uiState.isSaving
                ) {
                    AnimatedVisibility(visible = uiState.isSaving) {
                        Row {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Text(text = "Изменить состояние")
                }
            }
        }
    }
}