package ru.zarina.zarina.ui.screen.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaScreenPlaceholder
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun FavoritesScreen() {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    ForcedBottomNavBarBehavior(isVisible = true)

    ZarinaScreenPlaceholder(title = "Избранное")
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
