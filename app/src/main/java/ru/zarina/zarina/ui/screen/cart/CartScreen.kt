package ru.zarina.zarina.ui.screen.cart

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.ScreenPlaceholder
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview

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
    ZarinaPreview {
        // TODO: [High] Add preview
    }
}

