package com.example.course2.ui.composables.serviceRequest

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.course2.model.ServiceRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceRequestLazyColumn(
    servicesRequests: List<ServiceRequest>,
    onClick: (ServiceRequest) -> Unit,
    modifier: Modifier = Modifier,
    startContent: LazyListScope.() -> Unit = {},
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = state
    ) {
        startContent()
        items(
            servicesRequests,
            key = ServiceRequest::id
        ) {
            Column(
                modifier = Modifier.animateItemPlacement()
            ) {
                Divider()
                ServiceRequestSmallCard(
                    serviceRequest = it,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onClick(it)
                    }
                )
            }
        }
    }
}