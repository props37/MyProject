package ru.zarina.zarina.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.navigation.ZarinaNavigation

@Composable
fun ZarinaApp(
    splashScreen: SplashScreen,
) {
    val viewModel = koinViewModel<AppViewModel>()
    val startDestination by viewModel.startDestination.collectAsState()

    LaunchedEffect(startDestination) {
        splashScreen.setKeepOnScreenCondition { false }
    }

    ZarinaNavigation(
        startDestination = startDestination,
        changeStartDestination = remember { { viewModel.changeStartDestination(it) } },
    )
}
