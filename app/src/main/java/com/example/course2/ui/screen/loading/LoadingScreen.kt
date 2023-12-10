package com.example.course2.ui.screen.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.course2.api.ApiClient
import com.example.course2.api.ApiResult
import com.example.course2.api.RolesRepository
import com.example.course2.navigation.AppNavigation
import com.example.course2.navigation.navigateByRole
import com.example.course2.protobuf.authPreferencesStore
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Загрузка данных пользователя")
    }

    val navController = LocalNavController.current
    val errorProvider = LocalErrorProvider.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val rolesRepository = remember {
        RolesRepository(ApiClient.apiClient)
    }



    LaunchedEffect(Unit) {
        scope.launch {
            val authState = context.authPreferencesStore.data.first()
            ApiClient.token = authState.token
            delay(2000)

            when (val role = rolesRepository.getCurrentRole()) {
                is ApiResult.Error -> {
                    errorProvider.update {
                        it.copy(
                            message = role.error.message,
                            show = true
                        )
                    }
                }

                is ApiResult.Success -> {
                    navController.navigateByRole(role.data.name)
                }

                is ApiResult.UnAuth -> {
                    errorProvider.update {
                        it.copy(
                            message = "Для продолжения необходимо авторизоваться",
                            show = true
                        )
                    }
                    navController.navigate(AppNavigation.Account.route) {
                        popUpTo(0)
                    }

                }
            }

        }
    }
}