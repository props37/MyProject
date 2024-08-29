package ru.livetyping.zarina.util.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> rememberAnchoredDraggableState(
    initialValue: T,
    positionalThreshold: (totalDistance: Float) -> Float = PositionalThreshold,
    velocityThresholdDp: Dp = VelocityThreshold,
    snapAnimationSpec: AnimationSpec<Float> = tween(),
    decayAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay(),
    confirmValueChange: (newValue: T) -> Boolean = { true },
): AnchoredDraggableState<T> {
    val density = LocalDensity.current
    return remember(
        initialValue,
        positionalThreshold,
        velocityThresholdDp,
        snapAnimationSpec,
        decayAnimationSpec,
        confirmValueChange,
        density,
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThresholdDp.toPx() } },
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = decayAnimationSpec,
            confirmValueChange = confirmValueChange,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> rememberAnchoredDraggableState(
    initialValue: T,
    anchors: DraggableAnchors<T>,
    positionalThreshold: (totalDistance: Float) -> Float = PositionalThreshold,
    velocityThresholdDp: Dp = VelocityThreshold,
    snapAnimationSpec: AnimationSpec<Float> = tween(),
    decayAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay(),
    confirmValueChange: (newValue: T) -> Boolean = { true },
): AnchoredDraggableState<T> {
    val density = LocalDensity.current
    return remember(
        initialValue,
        anchors,
        positionalThreshold,
        velocityThresholdDp,
        snapAnimationSpec,
        decayAnimationSpec,
        confirmValueChange,
        density,
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            anchors = anchors,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThresholdDp.toPx() } },
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = decayAnimationSpec,
            confirmValueChange = confirmValueChange,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun <T> AnchoredDraggableState<T>.requireCoercedOffset(): Float {
    val minOffset = this.anchors.minAnchor()
    val maxOffset = this.anchors.maxAnchor()
    return this.requireOffset().coerceIn(minOffset, maxOffset)
}

/**
 * Strongly consider using [requireCoercedOffset] which will throw if the offset is read before
 * it is initialized.
 */
@OptIn(ExperimentalFoundationApi::class)
val <T> AnchoredDraggableState<T>.coercedOffset: Float
    get() {
        val offset = this.offset
        if (offset.isNaN()) return offset
        val minOffset = this.anchors.minAnchor()
        val maxOffset = this.anchors.maxAnchor()
        return offset.coerceIn(minOffset, maxOffset)
    }

@Stable
private val PositionalThreshold: (totalDistance: Float) -> Float = { it * 0.5f }

@Stable
private val VelocityThreshold: Dp
    get() = 125.dp
