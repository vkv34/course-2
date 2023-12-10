package com.example.course2.ui.composables.matianceRecord

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import com.example.course2.model.EquipmentMaintenanceRecord
import com.example.course2.ui.modifiers.shimmer

@Composable
fun MaintenanceRecordSmallCard(
    maintenanceRecord: EquipmentMaintenanceRecord,
    isLoading: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .heightIn(min = 10.dp)
                .widthIn(min = 150.dp)
                .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                .animateContentSize(),
            text = maintenanceRecord.equipmentCondition?.name ?: ""
        )
        Text(
            modifier = Modifier
                .heightIn(min = 10.dp)
                .widthIn(min = 150.dp)
                .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                .animateContentSize(),
            text = if (isLoading) "" else maintenanceRecord.date.toLocaleString()
        )
    }
}

@Composable
fun EquipmentMaintenanceRecordList(
    records: List<EquipmentMaintenanceRecord>,
    isLoading: Boolean = false
) {
    Column(
        Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        records.forEach { item: EquipmentMaintenanceRecord ->
            Divider(Modifier.fillMaxWidth())
            MaintenanceRecordSmallCard(
                maintenanceRecord = item,
                isLoading = false
            )
        }
        AnimatedVisibility(visible = isLoading) {
            repeat(1) {
//                Divider(Modifier.fillMaxWidth())
//                MaintenanceRecordSmallCard(
//                    maintenanceRecord = EquipmentMaintenanceRecord(),
//                    isLoading = isLoading
//                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .shimmer(isLoading, cornerRadius = CornerRadius(20f))
                )
            }
        }
    }
}