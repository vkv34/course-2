package com.example.course2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.course2.protobuf.authPreferencesStore
import com.example.course2.protobuf.model.AuthState
import com.example.course2.ui.LocalNavController
import com.example.course2.ui.localProviders.error.ErrorMessageState
import com.example.course2.ui.localProviders.error.LocalErrorProvider
import com.example.course2.ui.navigation.AppNavigation
import com.example.course2.ui.theme.Course2Theme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                val errorMessageHandler = remember {
                    ErrorMessageState()
                }
                Course2Theme {
                    // A surface container using the 'background' color from the theme
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CompositionLocalProvider(
                            LocalNavController provides navController,
                            LocalErrorProvider provides errorMessageHandler
                        ) {
                            AppNavigation(navHostController = navController)
                        }
                    }

                    val context = LocalContext.current

                    val authState: AuthState? by context.authPreferencesStore.data.collectAsState(
                        initial = null
                    )

                    LaunchedEffect(authState?.isAuth) {
                        if (authState?.isAuth == false) {
                            navController.navigate(com.example.course2.navigation.AppNavigation.Account.Auth.route) {
                                popUpTo(0)
                            }
                        }
                    }

                }

                val errorMessageState by errorMessageHandler.state.collectAsState()
                val scope = rememberCoroutineScope()
                var job: Job? by remember {
                    mutableStateOf(null)
                }
                LaunchedEffect(errorMessageState) {

                    if (errorMessageState.show) {
                        job?.cancel()
                        job = scope.launch {
                            delay(2000)
                            errorMessageHandler.update { it.copy(show = false) }
                        }
                    }

                }

                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .imePadding(),
                    visible = errorMessageState.show
                ) {
                    Card {
                        Column(
                            Modifier
                                .padding(8.dp)
                                .animateContentSize()
                        ) {
                            AnimatedContent(
                                targetState = errorMessageState,
                                label = ""
                            ) {
                                Column {
                                    if (it.title != null) {
                                        Text(
                                            modifier = Modifier.animateContentSize(),
                                            text = it.title
                                        )
                                    }
                                    if (it.message != null) {
                                        Text(
                                            modifier = Modifier.animateContentSize(),
                                            text = it.message
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

