package ru.zarina.zarina.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.util.compose.ScreenPlaceholder

@Composable
fun HomeScreen() {
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
