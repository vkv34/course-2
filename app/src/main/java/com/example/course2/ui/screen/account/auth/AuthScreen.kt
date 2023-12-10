package com.example.course2.ui.screen.account.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.course2.api.ApiClient
import com.example.course2.api.ApiRoutes
import com.example.course2.model.Employee
import com.example.course2.navigation.AppNavigation
import com.example.course2.protobuf.authPreferencesStore
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.modifiers.shimmer
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

@Composable
fun AuthScreen() {
    var authScreenState by remember {
        mutableStateOf(AuthScreenState())
    }
    val errorProvider = LocalErrorProvider.current
    val navController = LocalNavController.current

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = authScreenState.login,
            isError = !authScreenState.error.isNullOrBlank(),
            onValueChange = {
                authScreenState = authScreenState.copy(login = it)
            },
            label = { Text(text = "Логин") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = authScreenState.password,
            isError = !authScreenState.error.isNullOrBlank(),
            onValueChange = {
                authScreenState = authScreenState.copy(password = it)
            },
            label = { Text(text = "Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        AnimatedVisibility(visible = !authScreenState.error.isNullOrBlank()) {
            Text(
                text = authScreenState.error!!,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Button(
            modifier = Modifier.shimmer(
                authScreenState.isLoading
            ),
            enabled = !authScreenState.isLoading,
            onClick = {
                scope.launch {
                    authScreenState = authScreenState.copy(isLoading = true)
                    try {
                        val response =
                            ApiClient.apiClient.post(ApiRoutes.Account.AuthByLoginAndPassword.route) {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    Employee(
                                        login = authScreenState.login,
                                        password = authScreenState.password
                                    )
                                )
                            }

                        val result = response.body<String>().drop(1).dropLast(1)

                        if (!response.status.isSuccess()) {
                            errorProvider.update {
                                it.copy(
                                    title = "Ошибка",
                                    message = result,
                                    show = true
                                )
                            }
                            authScreenState = authScreenState.copy(
                                error = result,
                                password = "",
                                isLoading = false
                            )
                        } else {
                            ApiClient.token = result
                            val currentEmployeeResponse =
                                ApiClient.apiClient.get(ApiRoutes.Account.Current.route)

                            if (currentEmployeeResponse.status.isSuccess()) {
                                context.authPreferencesStore.updateData {
                                    it.toBuilder().setIsAuth(true).setToken(result)
                                        .setRole(currentEmployeeResponse.body<Employee>().role?.name)
                                        .build()
                                }

                                navController.navigate(AppNavigation.Loading.route) {
                                    popUpTo(AppNavigation.Loading.route)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        errorProvider.update {
                            it.copy(
                                show = true,
                                message = e.localizedMessage
                            )
                        }
                    }



                    authScreenState = authScreenState.copy(isLoading = false)

                }
            }) {
            Text(text = "Войти в приложение")
        }


    }
}