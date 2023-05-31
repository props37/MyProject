package ru.zarina.zarina.ui.common.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.layout.IntrinsicSizeOverride


@Composable
fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
    content: @Composable () -> Unit,
) {
    val foregroundColor by animateColorAsState(
        if (isEnabled) colors.foreground else colors.disabledForeground,
        label = "foreground color"
    )
    val backgroundColor by animateColorAsState(
        if (isEnabled) colors.background else colors.disabledBackground,
        label = "background color"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = foregroundColor),
                onClick = onClick,
                enabled = !isLoading && isEnabled,
            )
            .background(backgroundColor)
            .border(width = 1.dp, color = colors.border)
            .padding(padding)
            .height(IntrinsicSize.Min),
    ) {
        val contentAlpha by animateFloatAsState(
            targetValue = if (isLoading) 0f else 1f,
            label = "content alpha"
        )
        val loaderAlpha = 1f - contentAlpha
        if (loaderAlpha > 0f)
            Loader(
                color = foregroundColor,
                modifier = Modifier.graphicsLayer { alpha = loaderAlpha },
            )
        Box(
            modifier = Modifier.graphicsLayer { alpha = contentAlpha },
        ) {
            CompositionLocalProvider(LocalContentColor provides foregroundColor) {
                content()
            }
        }
    }
}

@Composable
private fun Loader(
    color: Color,
    modifier: Modifier = Modifier,
) {
    IntrinsicSizeOverride(minSize = 10.dp) {
        CircularProgressIndicator(
            modifier = modifier.size(16.dp),
            color = color,
            strokeWidth = 2.dp,
        )
    }
}

object ZarinaButtonDefaults {
    @Composable
    fun primaryColors(
        background: Color = UiKitTheme.colors.primaryButtonBackground,
        foreground: Color = UiKitTheme.colors.primaryButtonForeground,
        disabledBackground: Color = UiKitTheme.colors.primaryButtonDisabledBackground,
        disabledForeground: Color = UiKitTheme.colors.primaryButtonDisabledForeground,
        border: Color = UiKitTheme.colors.primaryButtonBorder,
    ) = ZarinaButtonColors(
        background = background,
        foreground = foreground,
        disabledBackground = disabledBackground,
        disabledForeground = disabledForeground,
        border = border,
    )

    @Composable
    fun secondaryColors(
        background: Color = UiKitTheme.colors.secondaryButtonBackground,
        foreground: Color = UiKitTheme.colors.secondaryButtonForeground,
        disabledBackground: Color = UiKitTheme.colors.secondaryButtonDisabledBackground,
        disabledForeground: Color = UiKitTheme.colors.secondaryButtonDisabledForeground,
        border: Color = UiKitTheme.colors.secondaryButtonBorder,
    ) = ZarinaButtonColors(
        background = background,
        foreground = foreground,
        disabledBackground = disabledBackground,
        disabledForeground = disabledForeground,
        border = border,
    )
}

data class ZarinaButtonColors(
    val background: Color,
    val disabledBackground: Color,
    val foreground: Color,
    val disabledForeground: Color,
    val border: Color,
)


@Preview(widthDp = 100, heightDp = 100, showBackground = true)
@Composable
private fun ZarinaButtonPrimaryPreview() {
    ZarinaButtonPreview(
        colors = ZarinaButtonDefaults.primaryColors(),
        isLoading = false,
    )
}

@Preview(widthDp = 100, heightDp = 100, showBackground = true)
@Composable
private fun ZarinaButtonSecondaryPreview() {
    ZarinaButtonPreview(
        colors = ZarinaButtonDefaults.secondaryColors(),
        isLoading = false
    )
}

@Preview(widthDp = 100, heightDp = 100, showBackground = true)
@Composable
private fun ZarinaButtonPrimaryLoadingPreview() {
    ZarinaButtonPreview(
        colors = ZarinaButtonDefaults.primaryColors(),
        isLoading = true,
    )
}

@Preview(widthDp = 100, heightDp = 100, showBackground = true)
@Composable
private fun ZarinaButtonSecondaryLoadingPreview() {
    ZarinaButtonPreview(
        colors = ZarinaButtonDefaults.secondaryColors(),
        isLoading = true
    )
}

@Composable
private fun ZarinaButtonPreview(
    colors: ZarinaButtonColors,
    isLoading: Boolean,
) {
    ZarinaTheme {
        Box(contentAlignment = Alignment.Center) {
            ZarinaButton(
                onClick = {},
                colors = colors,
                isLoading = isLoading,
            ) { }
        }
    }
}
