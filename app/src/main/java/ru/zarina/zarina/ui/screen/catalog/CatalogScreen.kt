package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.util.compose.ScreenPlaceholder

@Composable
fun CatalogScreen() {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    ForcedBottomNavBarBehavior(isVisible = true)

    ScreenPlaceholder(title = "Каталог")
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}
