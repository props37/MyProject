package ru.livetyping.zarina.core.uicompose.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn

internal object MaterialFadeThroughTransitions {
    fun enter(): EnterTransition {
        return fadeIn(
            animationSpec = tween(
                durationMillis = 210,
                delayMillis = 90,
                easing = FastOutSlowInEasing,
            )
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = 210,
                delayMillis = 90,
                easing = LinearOutSlowInEasing,
            ),
            initialScale = 0.92f,
        )
    }

    fun exit(): ExitTransition {
        return fadeOut(
            animationSpec = tween(
                durationMillis = 90,
                easing = FastOutSlowInEasing,
            )
        )
    }
}
