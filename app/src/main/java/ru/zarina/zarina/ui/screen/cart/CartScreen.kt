package ru.zarina.zarina.ui.screen.cart

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.ScreenPlaceholder

@Composable
fun CartScreen() {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    ForcedBottomNavBarBehavior(isVisible = true)

    ScreenPlaceholder(title = "Корзина")
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}

