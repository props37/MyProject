package ru.zarina.zarina.util.compose.collapsingtopbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import kotlin.math.roundToInt

@Composable
fun CollapsingTopBarLayout(
    topBar: @Composable () -> Unit,
    scrollBehavior: CollapsingTopBarScrollBehavior,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val topBarPlaceables = subcompose(LayoutContent.TopBar) {
                CollapsingTopBar(
                    scrollBehavior = scrollBehavior,
                    content = topBar,
                )
            }.map { it.measure(looseConstraints) }
            val topBarHeight = topBarPlaceables.maxByOrNull { it.height }?.height ?: 0

            val contentPlaceables = subcompose(LayoutContent.MainContent) {
                val innerPadding = PaddingValues(top = topBarHeight.toDp())
                content(innerPadding)
            }.map { it.measure(looseConstraints) }

            contentPlaceables.forEach { it.place(0, 0) }
            topBarPlaceables.forEach { it.place(0, 0) }
        }
    }
}

@Composable
private fun CollapsingTopBar(
    scrollBehavior: CollapsingTopBarScrollBehavior,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var contentHeightPx by remember { mutableIntStateOf(0) }

    val heightOffsetLimit = -contentHeightPx
    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != heightOffsetLimit.toFloat()) {
            scrollBehavior.state.heightOffsetLimit = heightOffsetLimit.toFloat()
        }
    }

    val heightPx = contentHeightPx + scrollBehavior.state.heightOffset

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        contentHeightPx = placeables.maxByOrNull { it.height }?.height ?: 0

        val layoutHeight = heightPx.roundToInt()
        layout(constraints.maxWidth, layoutHeight) {
            placeables.forEach {
                it.place(x = 0, y = layoutHeight - it.height)
            }
        }
    }
}

private enum class LayoutContent { MainContent, TopBar }
