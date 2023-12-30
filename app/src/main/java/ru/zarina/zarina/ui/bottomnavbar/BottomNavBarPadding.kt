package ru.zarina.zarina.ui.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
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

@Composable
fun bottomNavBarHeightAsState(): State<Dp> {
    val bottomNavBarSizeTracker = LocalBottomNavBarSizeTracker.current
    val bottomNavBarSizePxState = bottomNavBarSizeTracker.sizePx
    val density = LocalDensity.current
    return remember(bottomNavBarSizePxState, density) {
        derivedStateOf {
            with(density) { bottomNavBarSizePxState.value.height.toDp() }
        }
    }
}

fun Modifier.bottomNavBarPadding(): Modifier = this then BottomNavBarPaddingElement()

private class BottomNavBarPaddingElement : ModifierNodeElement<BottomNavBarPaddingNode>() {
    override fun create(): BottomNavBarPaddingNode {
        return BottomNavBarPaddingNode()
    }

    override fun update(node: BottomNavBarPaddingNode) {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

private class BottomNavBarPaddingNode : Modifier.Node(), LayoutModifierNode,
    CompositionLocalConsumerModifierNode {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val bottomNavBarSizeTracker = currentValueOf(LocalBottomNavBarSizeTracker)
        val bottomNavBarHeight = bottomNavBarSizeTracker.sizePx.value.height

        val placeable = measurable.measure(constraints.offset(vertical = -bottomNavBarHeight))

        val width = constraints.constrainWidth(placeable.width)
        val height = constraints.constrainHeight(placeable.height + bottomNavBarHeight)
        return layout(width, height) {
            placeable.placeRelative(0, 0)
        }
    }
}
