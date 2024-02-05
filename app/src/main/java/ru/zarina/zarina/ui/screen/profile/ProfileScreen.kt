package ru.zarina.zarina.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.ScreenPlaceholder
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun ProfileScreen() {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    ForcedBottomNavBarBehavior(isVisible = true)

    ScreenPlaceholder(title = "Профиль")
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
