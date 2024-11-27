package ru.livetyping.zarina.core.uikit.label

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaLabel(
    modifier: Modifier = Modifier,
    size: ZarinaLabelSize = ZarinaLabelSize.Large,
    colors: ZarinaLabelColors = ZarinaLabelDefaults.successColors(),
    textStyle: TextStyle = ZarinaLabelDefaults.textStyleFromSize(size),
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides textStyle,
        LocalContentColor provides colors.contentColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Indicator(colors = colors)

            val indicatorSpacer = when (size) {
                ZarinaLabelSize.Large -> ZarinaLabelDefaults.IndicatorSpacerLarge
                ZarinaLabelSize.Medium -> ZarinaLabelDefaults.IndicatorSpacerMedium
                ZarinaLabelSize.Small -> ZarinaLabelDefaults.IndicatorSpacerSmall
            }
            Spacer(modifier = Modifier.width(indicatorSpacer))

            content()
        }
    }
}

@Composable
private fun Indicator(
    colors: ZarinaLabelColors,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(colors.indicatorColor),
    )
}

public enum class ZarinaLabelSize { Large, Medium, Small }

public data class ZarinaLabelColors(
    val contentColor: Color,
    val indicatorColor: Color,
)

public object ZarinaLabelDefaults {
    public val Shape: Shape get() = RoundedCornerShape(2.dp)

    internal val SizeLarge: Dp get() = 24.dp
    internal val SizeMedium: Dp get() = 20.dp
    internal val SizeSmall: Dp get() = 16.dp

    internal val IndicatorSpacerLarge: Dp get() = 8.dp
    internal val IndicatorSpacerMedium: Dp get() = 6.dp
    internal val IndicatorSpacerSmall: Dp get() = 4.dp

    @Composable
    public fun successColors(
        contentColor: Color = UiKitTheme.colors.text.label.default,
        indicatorColor: Color = UiKitTheme.colors.text.label.success,
    ): ZarinaLabelColors = ZarinaLabelColors(
        contentColor = contentColor,
        indicatorColor = indicatorColor,
    )

    @Composable
    public fun warningColors(
        contentColor: Color = UiKitTheme.colors.text.label.default,
        indicatorColor: Color = UiKitTheme.colors.text.label.warning,
    ): ZarinaLabelColors = ZarinaLabelColors(
        contentColor = contentColor,
        indicatorColor = indicatorColor,
    )

    @Composable
    public fun dangerColors(
        contentColor: Color = UiKitTheme.colors.text.label.default,
        indicatorColor: Color = UiKitTheme.colors.text.label.danger,
    ): ZarinaLabelColors = ZarinaLabelColors(
        contentColor = contentColor,
        indicatorColor = indicatorColor,
    )

    @Composable
    public fun textStyleFromSize(size: ZarinaLabelSize): TextStyle = when (size) {
        ZarinaLabelSize.Large -> UiKitTheme.typography.tertiary.bold
        ZarinaLabelSize.Medium -> UiKitTheme.typography.footnote.bold
        ZarinaLabelSize.Small -> UiKitTheme.typography.caption2.bold
    }
}
