package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@Composable
internal fun TopBar(
    onBackClicked: () -> Unit,
    backgroundAlphaProvider: () -> Float,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(onClick = onBackClicked)
        },
        backgroundColor = UiKitTheme2.colors.white.copy(alpha = backgroundAlphaProvider()),
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
