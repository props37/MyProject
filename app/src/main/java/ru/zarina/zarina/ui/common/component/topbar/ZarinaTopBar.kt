package ru.zarina.zarina.ui.common.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.button.CloseIconButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaTopBar(
    modifier: Modifier = Modifier,
    startContent: (@Composable () -> Unit)? = null,
    centerContent: (@Composable () -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = TopBarDefaults.ContentPadding,
) {
    val content = @Composable {
        Box(modifier = Modifier.layoutId(LayoutId.StartContent)) { startContent?.invoke() }
        Box(modifier = Modifier.layoutId(LayoutId.CenterContent)) { centerContent?.invoke() }
        Box(modifier = Modifier.layoutId(LayoutId.EndContent)) { endContent?.invoke() }
    }

    Layout(
        content = content,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = TopBarDefaults.MinHeight)
            .padding(contentPadding),
    ) { measurables, constraints ->
        val startContentMeasurable = measurables.find { it.layoutId == LayoutId.StartContent }
        val centerContentMeasurable = measurables.find { it.layoutId == LayoutId.CenterContent }
        val endContentMeasurable = measurables.find { it.layoutId == LayoutId.EndContent }

        val centerContentConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val centerContentPlaceable = centerContentMeasurable?.measure(centerContentConstraints)
        val centerContentWidth = centerContentPlaceable?.width

        val sideContentConstraints = centerContentWidth?.let {
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = (constraints.maxWidth - centerContentWidth) / 2,
            )
        } ?: constraints.copy(minWidth = 0, minHeight = 0)
        val startContentPlaceable = startContentMeasurable?.measure(sideContentConstraints)
        val endContentPlaceable = endContentMeasurable?.measure(sideContentConstraints)

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

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaTopBar(
            startContent = {
                BackIconButton(onClick = {})
            },
            centerContent = {
                Text(
                    text = "Some title",
                    style = UiKitTheme.typographyReworked.primary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            },
            endContent = {
                CloseIconButton(onClick = {})
            },
            modifier = Modifier.background(Color.White),
        )
    }
}

private enum class LayoutId { StartContent, CenterContent, EndContent }
