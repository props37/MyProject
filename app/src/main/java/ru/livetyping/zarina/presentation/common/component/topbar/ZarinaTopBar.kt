package ru.livetyping.zarina.presentation.common.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

// TODO: [Medium] Add spacers between contents

@Composable
fun ZarinaTopBar(
    modifier: Modifier = Modifier,
    startContent: (@Composable RowScope.() -> Unit)? = null,
    centerContent: (@Composable RowScope.() -> Unit)? = null,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    contentColor: Color = UiKitTheme.colors.text.general.regular.default,
    contentPadding: PaddingValues = TopBarDefaults.ContentPadding,
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
                .heightIn(min = TopBarDefaults.MinHeight)
                .background(backgroundColor)
                .padding(contentPadding),
        ) { measurables, constraints ->
            val startContentMeasurable = measurables.find { it.layoutId == LayoutId.StartContent }
            val centerContentMeasurable = measurables.find { it.layoutId == LayoutId.CenterContent }
            val endContentMeasurable = measurables.find { it.layoutId == LayoutId.EndContent }

            val sideContentConstraints = constraints.copy(minWidth = 0, minHeight = 0)
            val startContentPlaceable = startContentMeasurable?.measure(sideContentConstraints)
            val endContentPlaceable = endContentMeasurable?.measure(sideContentConstraints)
            val sideContentMaxWidth = maxOf(
                startContentPlaceable?.width ?: 0,
                endContentPlaceable?.width ?: 0,
            )

            val centerContentMaxWidth = (constraints.maxWidth -
                    sideContentMaxWidth * 2 -
                    CenterContentHorizontalPadding.roundToPx() * 2)
                .coerceAtLeast(0)
            val centerContentConstraints = constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = centerContentMaxWidth,
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

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(onClick = {})
            },
            centerContent = {
                Text(
                    text = "Some title",
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            },
            endContent = {
                ZarinaCloseIconButton(onClick = {})
            },
        )
    }
}

private enum class LayoutId { StartContent, CenterContent, EndContent }

private val CenterContentHorizontalPadding: Dp get() = 16.dp
