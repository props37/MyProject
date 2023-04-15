package ru.zarina.zarina.ui.common.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.layout.IntrinsicSize


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = colors.foreground),
                onClick = onClick,
                enabled = !isLoading
            )
            .background(colors.background)
            .border(width = 1.dp, color = colors.border)
            .padding(vertical = 12.dp, horizontal = 24.dp),
    ) {
        AnimatedContent(
            targetState = isLoading,
            label = "button contents"
        ) { isLoading ->
            if (isLoading)
                Loader(
                    color = colors.foreground
                )
            else
                content()
        }
    }
}

@Composable
private fun Loader(
    color: Color,
    modifier: Modifier = Modifier,
) {
    IntrinsicSize(minSize = 10.dp) {
        CircularProgressIndicator(
            modifier = modifier,
            color = color,
            strokeWidth = 4.dp,
        )
    }
}

object ZarinaButtonDefaults {
    @Composable
    fun primaryColors(
        background: Color = UiKitTheme.colors.primaryButtonBackground,
        foreground: Color = UiKitTheme.colors.primaryButtonForeground,
        border: Color = UiKitTheme.colors.primaryButtonBorder,
    ) = ZarinaButtonColors(
        background = background,
        foreground = foreground,
        border = border,
    )

    @Composable
    fun secondaryColors(
        background: Color = UiKitTheme.colors.secondaryButtonBackground,
        foreground: Color = UiKitTheme.colors.secondaryButtonForeground,
        border: Color = UiKitTheme.colors.secondaryButtonBorder,
    ) = ZarinaButtonColors(
        background = background,
        foreground = foreground,
        border = border,
    )
}

data class ZarinaButtonColors(
    val background: Color,
    val foreground: Color,
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
