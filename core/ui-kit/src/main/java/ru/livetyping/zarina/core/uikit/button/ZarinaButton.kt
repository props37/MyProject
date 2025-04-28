package ru.livetyping.zarina.core.uikit.button

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.ripple.DarkRipple
import ru.livetyping.zarina.core.uikit.ripple.LightRipple
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
public fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    size: ZarinaButtonSize = ZarinaButtonSize.Large,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.filledColors(),
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
                .defaultMinSize(minSize, minSize)
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
    // TODO: [Top] Remove when migration is done
    public val Shape: Shape = RectangleShape

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

    @Composable
    public fun filledColors(
        backgroundColor: Color = UiKitTheme2.colors.mainBlack,
        contentColor: Color = UiKitTheme2.colors.white,
        disabledBackgroundColor: Color = backgroundColor,
        disabledContentColor: Color = contentColor,
        borderColor: Color = Color.Unspecified,
        disabledBorderColor: Color = borderColor,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    public fun outlinedColors(
        backgroundColor: Color = Color.Transparent,
        contentColor: Color = UiKitTheme2.colors.mainBlack,
        disabledBackgroundColor: Color = backgroundColor,
        disabledContentColor: Color = contentColor,
        borderColor: Color = UiKitTheme2.colors.mainBlack,
        disabledBorderColor: Color = borderColor,
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
        ZarinaButtonSize.Large -> UiKitTheme2.typography.body2
        ZarinaButtonSize.Medium -> UiKitTheme2.typography.body
        ZarinaButtonSize.Small -> UiKitTheme2.typography.body
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

    private const val MaxBackgroundColorLuminanceForLightRipple = 0.5f
}

private const val Tag = "ZarinaButton"
