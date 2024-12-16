package ru.livetyping.zarina.core.uicomponent.bottomnavbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.offset
import ru.livetyping.zarina.core.uicomponent.bottomnavbar.sizetracker.LocalBottomNavBarSizeTracker

@Composable
public fun bottomNavBarHeightAsState(): State<Dp> {
    val bottomNavBarSizeTracker = LocalBottomNavBarSizeTracker.current
    val bottomNavBarSizePx by bottomNavBarSizeTracker.sizePx
    val density = LocalDensity.current
    return remember(density) {
        derivedStateOf {
            with(density) { bottomNavBarSizePx.height.toDp() }
        }
    }
}

/**
 * @param windowInsets [WindowInsets] whose bottom padding will be used when calculating
 * the bottom padding. The bottom padding will be the largest value
 * of the height of the bottom navigation bar and the bottom padding of the [windowInsets].
 * The most common example will be [ime].
 */
public fun Modifier.bottomNavBarPadding(
    windowInsets: WindowInsets? = null,
): Modifier = this then BottomNavBarPaddingElement(windowInsets)

private class BottomNavBarPaddingElement(
    val windowInsets: WindowInsets?,
) : ModifierNodeElement<BottomNavBarPaddingNode>() {
    override fun create(): BottomNavBarPaddingNode {
        return BottomNavBarPaddingNode(windowInsets)
    }

    override fun update(node: BottomNavBarPaddingNode) {
        node.windowInsets = windowInsets
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

private class BottomNavBarPaddingNode(
    var windowInsets: WindowInsets?,
) : Modifier.Node(), LayoutModifierNode,
    CompositionLocalConsumerModifierNode {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val bottomNavBarSizeTracker = currentValueOf(LocalBottomNavBarSizeTracker)
        val bottomNavBarHeight = bottomNavBarSizeTracker.sizePx.value.height

        val bottomPadding = windowInsets?.let {
            val density = currentValueOf(LocalDensity)
            val windowInsetsBottomPadding = it.getBottom(density)
            (bottomNavBarHeight - windowInsetsBottomPadding).coerceAtLeast(0)
        } ?: bottomNavBarHeight

        val placeable = measurable.measure(constraints.offset(vertical = -bottomPadding))

        val width = constraints.constrainWidth(placeable.width)
        val height = constraints.constrainHeight(placeable.height + bottomPadding)
        return layout(width, height) {
            placeable.placeRelative(0, 0)
        }
    }
}
