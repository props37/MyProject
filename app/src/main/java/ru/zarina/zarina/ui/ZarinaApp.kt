package ru.zarina.zarina.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.navigation.ZarinaNavigation

@Composable
fun ZarinaApp(
    splashScreen: SplashScreen,
) {
    val viewModel = koinViewModel<AppViewModel>()
    val startDestination = viewModel.startDestination

    LaunchedEffect(startDestination) {
        splashScreen.setKeepOnScreenCondition { false }
    }

    ZarinaNavigation(
        startDestination = startDestination,
    )
}
