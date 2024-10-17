package ru.livetyping.zarina.core.ui.kit.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.ui.kit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.ui.kit.theme.UiKitTheme
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.DarkRipple
import ru.livetyping.zarina.core.uicompose.LightRipple

@Composable
public fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    size: ZarinaButtonSize = ZarinaButtonSize.Large,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
    shape: Shape = ZarinaButtonDefaults.Shape,
    contentPadding: PaddingValues = ZarinaButtonDefaults.contentPaddingFromSize(size),
    textStyle: TextStyle = ZarinaButtonDefaults.textStyleFromSize(size),
    indication: Indication? = ZarinaButtonDefaults.indicationFromColors(colors),
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

    CompositionLocalProvider(
        LocalTextStyle provides textStyle,
        LocalContentColor provides contentColor.value,
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
                    indication = indication,
                    enabled = isEnabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(contentPadding),
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
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

public data class ZarinaButtonColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val borderColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
    val disabledBorderColor: Color,
)

public enum class ZarinaButtonSize { Large, Medium, Small }

public object ZarinaButtonDefaults {
    public val Shape: Shape = RoundedCornerShape(2.dp)

    public val SizeLarge: Dp get() = 56.dp
    public val SizeMedium: Dp get() = 48.dp
    public val SizeSmall: Dp get() = 40.dp

    public val ContentPaddingLarge: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    public val ContentPaddingMedium: PaddingValues
        get() = PaddingValues(horizontal = 12.dp, vertical = 8.dp)

    public val ContentPaddingSmall: PaddingValues
        get() = PaddingValues(horizontal = 8.dp, vertical = 8.dp)

    public val ContentPaddingEven: PaddingValues
        get() = PaddingValues(8.dp)

    public val IconSizeLarge: Dp get() = 20.dp
    public val IconSizeMedium: Dp get() = 16.dp
    public val IconSizeSmall: Dp get() = 12.dp

    @Composable
    public fun primaryColors(
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
    public fun secondaryColors(
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
    public fun tertiaryColors(
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
    public fun outlineColors(
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
    public fun backlessColors(
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
    public fun outlineErrorColors(
        backgroundColor: Color = UiKitTheme.colors.background.button.outline.default,
        contentColor: Color = UiKitTheme.colors.text.button.error.default,
        disabledBackgroundColor: Color = UiKitTheme.colors.background.button.outline.disabled,
        disabledContentColor: Color = UiKitTheme.colors.text.button.error.disabled,
        borderColor: Color = UiKitTheme.colors.text.button.error.default,
        disabledBorderColor: Color = UiKitTheme.colors.text.button.error.disabled,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    public fun backlessErrorColors(
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
    internal fun contentPaddingFromSize(size: ZarinaButtonSize): PaddingValues = when (size) {
        ZarinaButtonSize.Large -> ContentPaddingLarge
        ZarinaButtonSize.Medium -> ContentPaddingMedium
        ZarinaButtonSize.Small -> ContentPaddingSmall
    }

    @Composable
    internal fun textStyleFromSize(size: ZarinaButtonSize): TextStyle = when (size) {
        ZarinaButtonSize.Large -> UiKitTheme.typography.tertiary.regular
        ZarinaButtonSize.Medium -> UiKitTheme.typography.caption1.regular
        ZarinaButtonSize.Small -> UiKitTheme.typography.caption3.regular
    }

    @Stable
    internal fun indicationFromColors(colors: ZarinaButtonColors): IndicationNodeFactory {
        val backgroundColor = colors.backgroundColor
        return if (backgroundColor.isSpecified) {
            val backgroundColorLuminance = colors.backgroundColor.luminance()
            if (backgroundColorLuminance <= MaxBackgroundColorLuminanceForLightRipple) {
                LightRipple
            } else {
                DarkRipple
            }
        } else {
            DarkRipple
        }
    }
}

private const val MaxBackgroundColorLuminanceForLightRipple = 0.5f

private const val Tag = "ZarinaButton"
