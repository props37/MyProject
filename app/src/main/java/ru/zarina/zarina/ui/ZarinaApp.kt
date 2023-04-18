package ru.zarina.zarina.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ru.zarina.zarina.ui.navigation.ZarinaNavigation

@Composable
fun ZarinaApp() {
    val viewModel = hiltViewModel<AppViewModel>()

    ZarinaNavigation(
        startDestination = viewModel.startDestination,
    )
}
