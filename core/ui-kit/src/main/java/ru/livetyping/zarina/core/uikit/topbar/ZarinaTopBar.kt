package ru.livetyping.zarina.core.uikit.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBarDefaults.ContentHorizontalPadding

@Composable
public fun ZarinaTopBar(
    modifier: Modifier = Modifier,
    startContent: (@Composable RowScope.() -> Unit)? = null,
    centerContent: (@Composable RowScope.() -> Unit)? = null,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    backgroundColor: Color = ZarinaTopBarDefaults.BackgroundColor,
    contentColor: Color = ZarinaTopBarDefaults.ContentColor,
    contentPadding: PaddingValues = ZarinaTopBarDefaults.ContentPadding,
) {
    val content = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.layoutId(LayoutId.StartContent),
        ) {
            startContent?.invoke(this)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.layoutId(LayoutId.CenterContent),
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides UiKitTheme.typography.primary.regular,
            ) {
                centerContent?.invoke(this)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.layoutId(LayoutId.EndContent),
        ) {
            endContent?.invoke(this)
        }
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Layout(
            content = content,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = ZarinaTopBarDefaults.MinHeight)
                .drawBehind { drawRect(backgroundColor) }
                .padding(contentPadding),
        ) { measurables, constraints ->
            val startContentMeasurable = measurables.find { it.layoutId == LayoutId.StartContent }
            val centerContentMeasurable = measurables.find { it.layoutId == LayoutId.CenterContent }
            val endContentMeasurable = measurables.find { it.layoutId == LayoutId.EndContent }

            val contentHorizontalPaddingPx = ContentHorizontalPadding.roundToPx()
            val sideContentMaxWidthConstraint =
                (constraints.maxWidth / 2 - contentHorizontalPaddingPx).coerceAtLeast(0)
            val sideContentConstraints = constraints.copy(
                minWidth = 0,
                maxWidth = sideContentMaxWidthConstraint,
                minHeight = 0,
            )
            val startContentPlaceable = startContentMeasurable?.measure(sideContentConstraints)
            val endContentPlaceable = endContentMeasurable?.measure(sideContentConstraints)
            val sideContentMaxWidth = maxOf(
                startContentPlaceable?.width ?: 0,
                endContentPlaceable?.width ?: 0,
            )

            val centerContentMaxWidthConstraint =
                (constraints.maxWidth - sideContentMaxWidth * 2 - contentHorizontalPaddingPx * 2)
                    .coerceAtLeast(0)
            val centerContentConstraints = constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = centerContentMaxWidthConstraint,
            )
            val centerContentPlaceable = centerContentMeasurable?.measure(centerContentConstraints)

            val contentMaxHeight = maxOf(
                startContentPlaceable?.height ?: 0,
                centerContentPlaceable?.height ?: 0,
                endContentPlaceable?.height ?: 0,
            )

            val layoutWidth = constraints.maxWidth
            val layoutHeight = maxOf(constraints.minHeight, contentMaxHeight)

            layout(layoutWidth, layoutHeight) {
                startContentPlaceable?.placeRelative(
                    x = 0,
                    y = (layoutHeight - startContentPlaceable.height) / 2,
                )
                centerContentPlaceable?.placeRelative(
                    x = layoutWidth / 2 - centerContentPlaceable.width / 2,
                    y = (layoutHeight - centerContentPlaceable.height) / 2,
                )
                endContentPlaceable?.placeRelative(
                    x = layoutWidth - endContentPlaceable.width,
                    y = (layoutHeight - endContentPlaceable.height) / 2,
                )
            }
        }
    }
}

@Composable
public fun ZarinaTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaTopBarDefaults.BackgroundColor,
    contentColor: Color = ZarinaTopBarDefaults.ContentColor,
    contentPadding: PaddingValues = ZarinaTopBarDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(ZarinaTopBarDefaults.MinHeight)
                .drawBehind { drawRect(backgroundColor) }
                .padding(contentPadding),
            content = content,
        )
    }
}

public object ZarinaTopBarDefaults {
    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    public val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    public val MinHeight: Dp = 56.dp

    public val HorizontalPadding: Dp get() = 16.dp
    public val VerticalPadding: Dp get() = 8.dp
    public val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = HorizontalPadding, vertical = VerticalPadding)

    public val ContentPaddingWithButtons: PaddingValues
        get() = PaddingValues(vertical = 4.dp)

    internal val ContentHorizontalPadding: Dp get() = 16.dp
}

private enum class LayoutId { StartContent, CenterContent, EndContent }
