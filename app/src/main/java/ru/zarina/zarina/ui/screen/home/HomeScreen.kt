package ru.zarina.zarina.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.ScreenPlaceholder

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    ForcedBottomNavBarBehavior(isVisible = true)

    ScreenPlaceholder(title = "Главная")
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}
