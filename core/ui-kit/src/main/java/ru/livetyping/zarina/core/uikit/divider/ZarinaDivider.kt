package ru.livetyping.zarina.core.uikit.divider

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaDivider(
    modifier: Modifier = Modifier,
    color: Color = ZarinaDividerDefaults.Color,
    thickness: Dp = ZarinaDividerDefaults.Thickness,
) {
    Divider(
        color = color,
        thickness = thickness,
        modifier = modifier,
    )
}

public object ZarinaDividerDefaults {
    public val Thickness: Dp = 0.5.dp

    public val Color: Color
        @Composable
        get() = UiKitTheme.colors.border.general.default
}
