package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@Composable
internal fun SuggestionListTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Text(
                text = title,
                style = UiKitTheme2.typography.h2,
            )
        },
        windowInsets = WindowInsets.none,
        modifier = modifier,
    )
}
