package ru.livetyping.zarina.presentation.common.component.tab

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.tab.LooseTabRowDefaults.looseTabIndicatorOffset
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaLooseTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    contentColor: Color = UiKitTheme.colors.background.general.inversed.default,
    indicator: @Composable (tabPositions: List<LooseTabPosition>) -> Unit = { tabPositions ->
        ZarinaTabIndicator(
            modifier = Modifier.looseTabIndicatorOffset(tabPositions[selectedTabIndex]),
        )
    },
    tabs: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        SubcomposeLayout(
            modifier = modifier
                .background(backgroundColor)
                .selectableGroup(),
        ) { constraints ->
            val tabMeasurables = subcompose(Slot.Tabs, tabs)
            val tabCount = tabMeasurables.size

            var availableTabWidth = constraints.maxWidth
            val tabPlaceables = tabMeasurables.map {
                @Suppress("NAME_SHADOWING")
                val constraints = constraints.copy(minWidth = 0, maxWidth = availableTabWidth)
                val placeable = it.measure(constraints)
                availableTabWidth -= placeable.width
                placeable
            }

            var tabPositionLeft = 0.dp
            val tabPositions = List(tabCount) { index ->
                val tabPlaceable = tabPlaceables[index]
                val tabWidthDp = tabPlaceable.width.toDp()
                val tabPosition = LooseTabPosition(tabPositionLeft, tabWidthDp)
                tabPositionLeft += tabWidthDp
                tabPosition
            }

            val tabRowWidth = tabPlaceables.sumOf { it.width }
            val tabRowHeight = tabPlaceables.maxByOrNull { it.height }?.height ?: 0

            layout(tabRowWidth, tabRowHeight) {
                var tabX = 0
                tabPlaceables.forEach { placeable ->
                    placeable.placeRelative(tabX, 0)
                    tabX += placeable.width
                }

                subcompose(Slot.Indicator) {
                    indicator(tabPositions)
                }.forEach { measurable ->
                    @Suppress("NAME_SHADOWING")
                    val constraints = Constraints.fixed(tabRowWidth, tabRowHeight)
                    val placeable = measurable.measure(constraints)
                    placeable.placeRelative(0, 0)
                }
            }
        }
    }
}

object LooseTabRowDefaults {
    fun Modifier.looseTabIndicatorOffset(
        currentTabPosition: LooseTabPosition,
        animationSpec: AnimationSpec<Dp> = tween(durationMillis = 250, easing = FastOutSlowInEasing),
    ): Modifier = composed(
        inspectorInfo = debugInspectorInfo {
            name = "tabIndicatorOffset"
            value = currentTabPosition
        }
    ) {
        val currentTabWidth by animateDpAsState(
            targetValue = currentTabPosition.width,
            animationSpec = animationSpec,
            label = "looseTabIndicatorOffset current tab width",
        )
        val indicatorOffset by animateDpAsState(
            targetValue = currentTabPosition.left,
            animationSpec = animationSpec,
            label = "looseTabIndicatorOffset indicator offset",
        )
        this
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            // TODO: [High] Migrate to offset {}?
            .offset(x = indicatorOffset)
            .width(currentTabWidth)
    }
}

@Immutable
class LooseTabPosition(val left: Dp, val width: Dp) {
    val right: Dp get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LooseTabPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width)"
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaLooseTabRow(selectedTabIndex = 0) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.backlessColors(),
            ) {
                Text(text = "Женщинам".uppercase())
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.backlessColors(),
            ) {
                Text(text = "Мужчинам".uppercase())
            }
        }
    }
}

private enum class Slot { Tabs, Indicator }
