package ru.zarina.zarina.util.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
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
    animationSpec: AnimationSpec<Float> = spring(),
    confirmValueChange: (newValue: T) -> Boolean = { true },
): AnchoredDraggableState<T> {
    val density = LocalDensity.current
    return remember(
        initialValue,
        positionalThreshold,
        velocityThresholdDp,
        animationSpec,
        confirmValueChange,
        density,
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThresholdDp.toPx() } },
            animationSpec = animationSpec,
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
    animationSpec: AnimationSpec<Float> = spring(),
    confirmValueChange: (newValue: T) -> Boolean = { true },
): AnchoredDraggableState<T> {
    val density = LocalDensity.current
    return remember(
        initialValue,
        anchors,
        positionalThreshold,
        velocityThresholdDp,
        animationSpec,
        confirmValueChange,
        density,
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            anchors = anchors,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThresholdDp.toPx() } },
            animationSpec = animationSpec,
            confirmValueChange = confirmValueChange,
        )
    }
}

@Stable
private val PositionalThreshold: (totalDistance: Float) -> Float
    get() = { it * 0.5f }

@Stable
private val VelocityThreshold: Dp
    get() = 125.dp
