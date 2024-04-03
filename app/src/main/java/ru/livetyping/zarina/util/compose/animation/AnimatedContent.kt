package ru.livetyping.zarina.util.compose.animation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Stable

@Stable
val AnimatedContentDefaultEnterTransition: EnterTransition
    get() = fadeIn(
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    )

@Stable
val AnimatedContentDefaultExitTransition: ExitTransition
    get() = fadeOut(animationSpec = tween(durationMillis = 90))

@Stable
val AnimatedContentDefaultTransitionSpec: () -> ContentTransform
    get() = {
        AnimatedContentDefaultEnterTransition togetherWith AnimatedContentDefaultExitTransition
    }

@Stable
val AnimatedContentCrossfadeTransitionSpec: () -> ContentTransform
    get() = {
        val animationSpec = tween<Float>()
        fadeIn(animationSpec) togetherWith fadeOut(animationSpec)
    }
