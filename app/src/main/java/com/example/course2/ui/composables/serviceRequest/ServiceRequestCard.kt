package com.example.course2.ui.composables.serviceRequest

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.course2.model.Equipment
import com.example.course2.model.Location
import com.example.course2.model.ServiceRequest
import com.example.course2.ui.modifiers.shimmer
import java.util.Date

@Composable
fun ServiceRequestSmallCard(
    serviceRequest: ServiceRequest,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.then(
            Modifier.clickable {
                onClick()
            }
        ),
        headlineContent = {
            Text(text = serviceRequest.name.ifEmpty {
                serviceRequest.equipment?.equipmentName ?: ""
            })
        },
        supportingContent = {
            Column {
                Text(text = serviceRequest.equipment?.location?.name ?: "Место не указано")
                Text(text = serviceRequest.reason)
            }
        },
        overlineContent = {
            Text(text = serviceRequest.date.toLocaleString())
        }

    )
}

@Preview
@Composable
fun ServiceRequestCardPreview() {
    val serviceRequest = remember {
        ServiceRequest(
            date = Date(System.currentTimeMillis()),
            name = "Проверить стол",
            reason = "Скрипит правая ножка",
            equipment = Equipment(
                location = Location(
                    name = "Помещение 1"
                )
            )
        )
    }

    ServiceRequestSmallCard(serviceRequest = serviceRequest) {

    }

}

@Composable
fun ServiceRequestCard(
    serviceRequest: ServiceRequest,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .animateContentSize()
        ,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = serviceRequest.name.ifEmpty {
                serviceRequest.equipment?.equipmentName ?: ""
            },
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
            modifier = Modifier.fillMaxWidth()
                .animateContentSize()
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Дата обращения: "
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
                    .animateContentSize()
                ,
                text = if (isLoading) "" else serviceRequest.date.toLocaleString()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize()
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Место: "
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
                    .animateContentSize()
                ,
                text = serviceRequest.equipment?.location?.name ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize()
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Комментарий: "
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
                    .animateContentSize()
                ,
                text = serviceRequest.reason
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize()
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignBy(FirstBaseline),
                text = "Оборудование: "
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
                    .animateContentSize()
                ,
                text = serviceRequest.equipment?.equipmentName ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize()
            ,
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
                    .animateContentSize()
                ,
                text = serviceRequest.equipment?.inventoryNumber ?: ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Preview
@Composable
fun ServiceRequestLargeCardPreview() {
    val serviceRequest = remember {
        ServiceRequest(
            date = Date(System.currentTimeMillis()),
            name = "Проверить стол",
            reason = "Скрипит правая ножка",
            equipment = Equipment(
                location = Location(
                    name = "Помещение 1"
                )
            )
        )
    }

    ServiceRequestSmallCard(serviceRequest = serviceRequest) {

    }
}