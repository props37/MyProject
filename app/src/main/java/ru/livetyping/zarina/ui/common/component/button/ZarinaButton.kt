package ru.livetyping.zarina.ui.common.component.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.ui.common.rippletheme.DarkRippleTheme
import ru.livetyping.zarina.ui.common.rippletheme.LightRippleTheme
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.ui.theme.ZarinaTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.defaultMinSize

@Composable
fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    size: ZarinaButtonSize = ZarinaButtonSize.Large,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
    shape: Shape = ZarinaButtonDefaults.Shape,
    contentPadding: PaddingValues = ZarinaButtonDefaults.contentPaddingFromSize(size),
    textStyle: TextStyle = ZarinaButtonDefaults.textStyleFromSize(size),
    isIndicationEnabled: Boolean = true,
    useProvidedRippleTheme: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val minSize = when (size) {
        ZarinaButtonSize.Large -> ZarinaButtonDefaults.SizeLarge
        ZarinaButtonSize.Medium -> ZarinaButtonDefaults.SizeMedium
        ZarinaButtonSize.Small -> ZarinaButtonDefaults.SizeSmall
    }

    val backgroundColor = animateColorAsState(
        targetValue = if (isEnabled) colors.backgroundColor else colors.disabledBackgroundColor,
        label = "$Tag background color",
    )
    val contentColor = animateColorAsState(
        targetValue = if (isEnabled) colors.contentColor else colors.disabledContentColor,
        label = "$Tag content color",
    )
    val borderColor = animateColorAsState(
        targetValue = if (isEnabled) colors.borderColor else colors.disabledBorderColor,
        label = "$Tag border color",
    )

    val providedRippleTheme = LocalRippleTheme.current
    val rippleTheme = remember(
        useProvidedRippleTheme,
        providedRippleTheme,
        colors.backgroundColor,
    ) {
        if (useProvidedRippleTheme) {
            providedRippleTheme
        } else {
            val backgroundColorLuminance = colors.backgroundColor.luminance()
            when {
                colors.backgroundColor.isUnspecified -> DarkRippleTheme
                backgroundColorLuminance <= MaxBackgroundColorLuminanceForLightRippleTheme -> {
                    LightRippleTheme
                }

                else -> DarkRippleTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalTextStyle provides textStyle,
        LocalContentColor provides contentColor.value,
        LocalRippleTheme provides rippleTheme,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .defaultMinSize(minSize)
                .clip(shape)
                .drawBehind { drawRect(backgroundColor.value) }
                .border(width = 1.dp, color = borderColor.value, shape = shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = if (isIndicationEnabled) LocalIndication.current else null,
                    enabled = isEnabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(contentPadding),
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "ZarinaButton content",
            ) { isLoading ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isLoading) {
                        content()
                    } else {
                        ZarinaCircularLoader(
                            color = contentColor.value,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    }
}

data class ZarinaButtonColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
)

enum class ZarinaButtonSize { Large, Medium, Small }

object ZarinaButtonDefaults {
    val Shape = RoundedCornerShape(2.dp)

    val SizeLarge: Dp get() = 56.dp
    val SizeMedium: Dp get() = 48.dp
    val SizeSmall: Dp get() = 40.dp

    val ContentPaddingLarge: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    val ContentPaddingMedium: PaddingValues
        get() = PaddingValues(horizontal = 12.dp, vertical = 8.dp)

    val ContentPaddingSmall: PaddingValues
        get() = PaddingValues(horizontal = 8.dp, vertical = 8.dp)

    val ContentPaddingEven: PaddingValues
        get() = PaddingValues(8.dp)

    val IconSizeLarge: Dp get() = 20.dp
    val IconSizeMedium: Dp get() = 16.dp
    val IconSizeSmall: Dp get() = 12.dp

    @Composable
    fun primaryColors(
        backgroundColor: Color = UiKitTheme.colors.background.button.primary.default,
        contentColor: Color = UiKitTheme.colors.text.button.primary.default,
        disabledBackgroundColor: Color = UiKitTheme.colors.background.button.primary.disabled,
        disabledContentColor: Color = UiKitTheme.colors.text.button.primary.disabled,
        borderColor: Color = Color.Unspecified,
        disabledBorderColor: Color = Color.Unspecified,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    fun secondaryColors(
        backgroundColor: Color = UiKitTheme.colors.background.button.secondary.default,
        contentColor: Color = UiKitTheme.colors.text.button.secondary.default,
        disabledBackgroundColor: Color = UiKitTheme.colors.background.button.secondary.disabled,
        disabledContentColor: Color = UiKitTheme.colors.text.button.secondary.disabled,
        borderColor: Color = Color.Unspecified,
        disabledBorderColor: Color = Color.Unspecified,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    fun tertiaryColors(
        backgroundColor: Color = UiKitTheme.colors.background.button.tertiary.default,
        contentColor: Color = UiKitTheme.colors.text.button.tertiary.default,
        disabledBackgroundColor: Color = UiKitTheme.colors.background.button.tertiary.disabled,
        disabledContentColor: Color = UiKitTheme.colors.text.button.tertiary.disabled,
        borderColor: Color = Color.Unspecified,
        disabledBorderColor: Color = Color.Unspecified,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    fun outlineColors(
        backgroundColor: Color = UiKitTheme.colors.background.button.outline.default,
        contentColor: Color = UiKitTheme.colors.text.button.outline.default,
        disabledBackgroundColor: Color = UiKitTheme.colors.background.button.outline.disabled,
        disabledContentColor: Color = UiKitTheme.colors.text.button.outline.disabled,
        borderColor: Color = UiKitTheme.colors.border.button.default,
        disabledBorderColor: Color = UiKitTheme.colors.border.button.disabled,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    fun backlessColors(
        backgroundColor: Color = Color.Unspecified,
        contentColor: Color = UiKitTheme.colors.text.button.backless.default,
        disabledBackgroundColor: Color = Color.Unspecified,
        disabledContentColor: Color = UiKitTheme.colors.text.button.backless.disabled,
        borderColor: Color = Color.Unspecified,
        disabledBorderColor: Color = Color.Unspecified,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    fun backlessErrorColors(
        backgroundColor: Color = Color.Unspecified,
        contentColor: Color = UiKitTheme.colors.text.button.error.default,
        disabledBackgroundColor: Color = Color.Unspecified,
        disabledContentColor: Color = UiKitTheme.colors.text.button.error.disabled,
        borderColor: Color = Color.Unspecified,
        disabledBorderColor: Color = Color.Unspecified,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Stable
    fun contentPaddingFromSize(size: ZarinaButtonSize): PaddingValues = when (size) {
        ZarinaButtonSize.Large -> ContentPaddingLarge
        ZarinaButtonSize.Medium -> ContentPaddingMedium
        ZarinaButtonSize.Small -> ContentPaddingSmall
    }

    @Composable
    fun textStyleFromSize(size: ZarinaButtonSize): TextStyle = when (size) {
        ZarinaButtonSize.Large -> UiKitTheme.typography.tertiary.regular
        ZarinaButtonSize.Medium -> UiKitTheme.typography.caption1.regular
        ZarinaButtonSize.Small -> UiKitTheme.typography.caption3.regular
    }
}

@Preview
@Composable
private fun Primary() {
    ZarinaTheme {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.primaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "PRIMARY LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.primaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "PRIMARY MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.primaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "PRIMARY SMALL")
            }
        }
    }
}

@Preview
@Composable
private fun Secondary() {
    ZarinaTheme {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.secondaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "SECONDARY LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.secondaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "SECONDARY MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.secondaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "SECONDARY SMALL")
            }
        }
    }
}

@Preview
@Composable
private fun Tertiary() {
    ZarinaTheme {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "TERTIARY LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "TERTIARY MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "TERTIARY SMALL")
            }
        }
    }
}

@Preview
@Composable
private fun Outline() {
    ZarinaTheme {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "OUTLINE LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "OUTLINE MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "OUTLINE SMALL")
            }
        }
    }
}

private const val MaxBackgroundColorLuminanceForLightRippleTheme = 0.5f

private const val Tag = "ZarinaButton"
