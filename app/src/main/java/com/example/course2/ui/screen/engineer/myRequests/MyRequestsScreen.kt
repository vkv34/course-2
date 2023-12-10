package com.example.course2.ui.screen.engineer.myRequests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.course2.navigation.AppNavigation
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.composables.filter.SearchOptionButton
import com.example.course2.ui.composables.serviceRequest.ServiceRequestLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen() {
    val viewModel: MyRequestsScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val requests by viewModel.request.collectAsState()
    val searchRequests by viewModel.displayRequests.collectAsState(listOf())
    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navController = LocalNavController.current


    Column(
        Modifier.fillMaxSize(),
    ) {
        TopAppBar(
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
            title = { Text(text = "Список заявок") },
            scrollBehavior = scrollBehavior
        )
        Column(
            Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {

            ServiceRequestLazyColumn(
                servicesRequests = searchRequests,
                onClick = {
                    navController.navigate(
                        AppNavigation.Engeener.RequestDetailsScreen.putId(
                            it.id
                        )
                    )
                },
                startContent = {
                    item {
                        Column(
                            Modifier.fillMaxWidth()
                        ) {
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
                                    label = {
                                        Text(text = "Поиск")
                                    }
                                )
                                SearchOptionButton(
                                    parameters = listOf(
                                        SortParameter,
                                        FilterParameter
                                    ),
                                    icon = Icons.Default.Menu,
                                    modifier = Modifier.weight(0.2f),
                                    onOptionSelectionChanged = viewModel::updateParameters
                                )

                            }
                            Spacer(modifier = Modifier.height(16.dp))

                        }

                    }
                }
            )
        }
    }
}