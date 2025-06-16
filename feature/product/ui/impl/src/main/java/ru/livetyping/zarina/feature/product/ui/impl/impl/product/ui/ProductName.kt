package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
internal fun ProductName(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name.uppercase(),
        style = UiKitTheme2.typography.h4,
        color = UiKitTheme2.colors.mainBlack,
        modifier = modifier,
    )
}
