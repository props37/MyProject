package ru.livetyping.zarina.core.uicompose.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

internal object MaterialSharedAxisTransitions {
    fun enterX(forward: Boolean, density: Density): EnterTransition {
        val offsetSign = if (forward) 1 else -1
        val offset = with(density) { SHARED_AXIS_OFFSET.roundToPx() }
        return slideInHorizontally(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            initialOffsetX = { offsetSign * offset },
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 210,
                delayMillis = 90,
                easing = LinearOutSlowInEasing,
            )
        )
    }

    fun exitX(forward: Boolean, density: Density): ExitTransition {
        val offsetSign = if (forward) 1 else -1
        val offset = with(density) { SHARED_AXIS_OFFSET.roundToPx() }
        return slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            targetOffsetX = { offsetSign * -offset },
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 90,
                easing = FastOutLinearInEasing,
            )
        )
    }

    fun enterY(forward: Boolean, density: Density): EnterTransition {
        val offsetSign = if (forward) 1 else -1
        val offset = with(density) { SHARED_AXIS_OFFSET.roundToPx() }
        return slideInVertically(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            initialOffsetY = { offsetSign * offset },
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 210,
                delayMillis = 90,
                easing = LinearOutSlowInEasing,
            )
        )
    }

    fun exitY(forward: Boolean, density: Density): ExitTransition {
        val offsetSign = if (forward) 1 else -1
        val offset = with(density) { SHARED_AXIS_OFFSET.roundToPx() }
        return slideOutVertically(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            targetOffsetY = { offsetSign * -offset },
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 90,
                easing = FastOutLinearInEasing,
            )
        )
    }

    fun enterZ(forward: Boolean): EnterTransition {
        val initialScale = if (forward) 0.8f else 1.1f
        return scaleIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            initialScale = initialScale,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 210,
                delayMillis = 90,
                easing = LinearOutSlowInEasing,
            ),
        )
    }

    fun exitZ(forward: Boolean): ExitTransition {
        val targetScale = if (forward) 1.1f else 0.8f
        return scaleOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            targetScale = targetScale,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 90,
                easing = FastOutLinearInEasing,
            )
        )
    }

    private val SHARED_AXIS_OFFSET = 30.dp
}
