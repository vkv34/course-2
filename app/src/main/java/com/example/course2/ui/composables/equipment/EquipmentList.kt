package com.example.course2.ui.composables.equipment

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.course2.model.Equipment

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipmentList(
    records: List<Equipment>,
    isLoading: Boolean = false,
    onEquipmentClick: (Equipment) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        if (isLoading) {
            repeat(2) {
                item(it) {
                    Divider(Modifier.fillMaxWidth())
                    EquipmentSmallCard(
                        equipment = Equipment(),
                        isLoading = true
                    ) {
                    }
                }
            }
        }
        item("Spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
        records.forEach { item ->
            item(key = item.inventoryNumber) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                ) {
                    Divider(Modifier.fillMaxWidth())
                    EquipmentSmallCard(
                        equipment = item,
                        isLoading = false
                    ) {
                        onEquipmentClick(item)
                    }
                }
            }

        }

    }
}