package ru.zarina.zarina.ui.common.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.conditional


@Composable
fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
    content: @Composable () -> Unit,
) {
    // TODO reverted ripple
    val isBorderNecessary = colors.border != colors.background
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .background(colors.background)
            .conditional(isBorderNecessary) {
                border(width = 1.dp, color = colors.border)
            }
            .padding(vertical = 12.dp, horizontal = 24.dp),
    ) {
        content()
    }
}

object ZarinaButtonDefaults {
    @Composable
    fun primaryColors(
        background: Color = UiKitTheme.colors.primaryButtonBackground,
        foreground: Color = UiKitTheme.colors.primaryButtonForeground,
        border: Color = UiKitTheme.colors.primaryButtonBorder,
        isRippleLight: Boolean = true,
    ) = ZarinaButtonColors(
        background = background,
        foreground = foreground,
        border = border,
        isRippleLight = isRippleLight,
    )

    @Composable
    fun secondaryColors(
        background: Color = UiKitTheme.colors.secondaryButtonBackground,
        foreground: Color = UiKitTheme.colors.secondaryButtonForeground,
        border: Color = UiKitTheme.colors.secondaryButtonBorder,
        isRippleLight: Boolean = true,
    ) = ZarinaButtonColors(
        background = background,
        foreground = foreground,
        border = border,
        isRippleLight = isRippleLight,
    )
}

data class ZarinaButtonColors(
    val background: Color,
    val foreground: Color,
    val border: Color,
    val isRippleLight: Boolean = background.luminance() > 0.5f,
)


@Preview(widthDp = 100, heightDp = 100, showBackground = true)
@Composable
private fun ZarinaButtonPrimaryPreview() {
    ZarinaButtonPreview(colors = ZarinaButtonDefaults.primaryColors())
}

@Preview(widthDp = 100, heightDp = 100, showBackground = true)
@Composable
private fun ZarinaButtonSecondaryPreview() {
    ZarinaButtonPreview(colors = ZarinaButtonDefaults.secondaryColors())
}

@Composable
private fun ZarinaButtonPreview(
    colors: ZarinaButtonColors,
) {
    ZarinaTheme {
        Box(contentAlignment = Alignment.Center) {
            ZarinaButton(
                onClick = {},
                colors = colors,
            ) { }
        }
    }
}
