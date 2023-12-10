package com.example.course2.ui.composables.equipment

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.course2.model.Equipment
import com.example.course2.ui.modifiers.shimmer
import kotlinx.coroutines.delay

@Composable
fun EquipmentLargeCard(
    equipment: Equipment,
    isLoading: Boolean = false
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = equipment.equipmentName,
            modifier = Modifier
                .shimmer(isLoading, cornerRadius = CornerRadius(20f))
                .then(
                    if (isLoading) {
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                    } else {
                        Modifier
                    }
                )
                .animateContentSize(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Срок годности: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.shelfLife?.toLocaleString()
                    ?: "Без срока годности"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Инвентарный номер: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.inventoryNumber ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Состояние: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.condition?.name ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ExtendedEquipmentLargeCard(
    equipment: Equipment,
    isLoading: Boolean = false
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = equipment.equipmentName,
            modifier = Modifier
                .shimmer(isLoading, cornerRadius = CornerRadius(20f))
                .then(
                    if (isLoading) {
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                    } else {
                        Modifier
                    }
                )
                .animateContentSize(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Инвентарный\nномер:"
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.inventoryNumber ?: "",
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Срок годности: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.shelfLife?.toLocaleString()
                    ?: "Без срока годности"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Стоиость закупки: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.purchaseCost.toString() ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Рыночная стоимость: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.marketValue.toString() ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Дата закупки: "
            )
            Text(
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .shimmer(isLoading, cornerRadius = CornerRadius(25f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
                    .animateContentSize(),
                text = if (isLoading) "" else equipment.purchaseDate.toLocaleString() ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EquipmentSmallCard(
    equipment: Equipment,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.then(
            Modifier
                .clickable {
                    onClick()
                }
                .animateContentSize()
        ),
        headlineContent = {
            Text(
                text = if (isLoading) "" else equipment.equipmentName,
                modifier = Modifier
                    .shimmer(isLoading, cornerRadius = CornerRadius(10f))
                    .then(
                        if (isLoading) {
                            Modifier
                                .height(20.dp)
                                .width(150.dp)
                        } else {
                            Modifier
                        }
                    )
            )
        },
        supportingContent = {
            Column {
                if (isLoading) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(
                    text = if (isLoading) "" else equipment.inventoryNumber,
                    modifier = Modifier
                        .then(
                            if (isLoading) {
                                Modifier
                                    .height(20.dp)
                                    .width(250.dp)
                                    .shimmer(isLoading, cornerRadius = CornerRadius(10f))
                                    .padding(top = 16.dp)
                            } else {
                                Modifier
                            }
                        )
                )
            }
        },
    )
}

@Preview
@Composable
private fun SmallCardPrev() {
    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
    }
    EquipmentSmallCard(
        equipment = Equipment(
            equipmentName = "Equipment",
            inventoryNumber = "00000000-0000-0000-0000-000000000001"
        ), isLoading = isLoading
    ) {

    }
}