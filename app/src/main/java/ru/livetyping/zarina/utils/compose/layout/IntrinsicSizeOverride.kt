package ru.livetyping.zarina.utils.compose.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

@Composable
fun IntrinsicSizeOverride(
    minSize: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    val measurePolicy = minIntrinsicMeasurePolicy(minWidth = minSize, minHeight = minSize)
    Layout(content = content, measurePolicy = measurePolicy, modifier = modifier)
}


@Composable
fun minIntrinsicMeasurePolicy(
    minWidth: Dp? = null,
    minHeight: Dp? = null,
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
): ForcedIntrinsicMeasurePolicy {
    with(LocalDensity.current) {
        return ForcedIntrinsicMeasurePolicy(
            minIntrinsicWidth = minWidth?.toPx()?.roundToInt(),
            minIntrinsicHeight = minHeight?.toPx()?.roundToInt(),
            maxIntrinsicWidth = maxWidth?.toPx()?.roundToInt(),
            maxIntrinsicHeight = maxHeight?.toPx()?.roundToInt()
        )
    }
}

open class DefaultMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        val placeables = measurables.map { it.measure(constraints) }
        val width = placeables.maxByOrNull { it.width }?.width ?: 0
        val height = placeables.maxByOrNull { it.height }?.height ?: 0
        return layout(width, height) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
        }
    }
}

class ForcedIntrinsicMeasurePolicy(
    val minIntrinsicWidth: Int?,
    val minIntrinsicHeight: Int?,
    val maxIntrinsicWidth: Int?,
    val maxIntrinsicHeight: Int?,
) : DefaultMeasurePolicy() {
    private val defaultImplementation = DefaultMeasurePolicy()

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int {
        return minIntrinsicWidth ?: with(defaultImplementation) {
            this@minIntrinsicWidth.minIntrinsicWidth(measurables, height)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int {
        return minIntrinsicHeight ?: with(defaultImplementation) {
            this@minIntrinsicHeight.minIntrinsicHeight(measurables, width)
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int {
        return maxIntrinsicHeight ?: with(defaultImplementation) {
            this@maxIntrinsicHeight.maxIntrinsicHeight(measurables, width)
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int {
        return maxIntrinsicWidth ?: with(defaultImplementation) {
            this@maxIntrinsicWidth.maxIntrinsicWidth(measurables, height)
        }
    }
}
