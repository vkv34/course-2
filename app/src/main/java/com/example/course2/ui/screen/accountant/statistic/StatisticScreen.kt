package com.example.course2.ui.screen.accountant.statistic

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.db.williamchart.view.BarChartView
import com.example.course2.api.apiClient
import com.example.course2.model.Equipment
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.composables.dropdown.DropDownMenu
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticScreen() {
    Column(
        Modifier.fillMaxSize()
    ) {
        val navController = LocalNavController.current
        val scope = rememberCoroutineScope()
        val mutableEquipments = remember {
            MutableStateFlow(listOf<Equipment>())
        }
        val equipment by mutableEquipments.collectAsState()

        var year by remember {
            mutableIntStateOf(2023)
        }

        LaunchedEffect(Unit) {
            scope.launch {
                launch {
                    mutableEquipments.update {
                        apiClient.get("Equipment/").body()
                    }
                    year = (equipment.map(Equipment::purchaseDate)
                        .maxOfOrNull(Date::getYear)?.plus(1900)) ?: 2023
                }
            }
        }

        TopAppBar(
            title = { Text(text = "Расход по месяцам") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = Icons.Default.ExitToApp.name
                    )
                }
            },
        )
        Column(
            Modifier.fillMaxWidth()
        ) {

            DropDownMenu(
                options = equipment
                    .map(Equipment::purchaseDate)
                    .map(Date::getYear)
                    .distinct()
                    .map { it.plus(1900) },
                selectedOption = year,
                onSelectionChanged = { year = it },
                displayValue = { it.toString() },
                title = "Год"
            )

            AndroidView(
                modifier = Modifier.padding(8.dp)
                    .padding(vertical = 128.dp)
                ,
                factory = { viewContext ->
                BarChartView(viewContext)
                    .apply {
                    layoutParams = ViewGroup.LayoutParams(-1, -1)

                }

            }) { chart ->
                chart.animate(
                    equipment
                        .groupBy { it.purchaseDate.month }
                        .map { groupMap ->

                            val purchaseSum = groupMap.value
                                .filter { it.purchaseDate.year == year - 1900 }
                                .map(Equipment::purchaseCost)
                                .sum()

                            groupMap.key.toString() to purchaseSum.toFloat()
                        }
                )

            }
        }

    }

}
