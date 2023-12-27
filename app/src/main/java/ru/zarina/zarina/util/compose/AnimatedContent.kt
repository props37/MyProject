package ru.zarina.zarina.util.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith

val AnimatedContentDefaultEnterTransition: EnterTransition
    get() = fadeIn(
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    )

val AnimatedContentDefaultExitTransition: ExitTransition
    get() = fadeOut(animationSpec = tween(durationMillis = 90))

val AnimatedContentDefaultTransitionSpec = {
    AnimatedContentDefaultEnterTransition togetherWith AnimatedContentDefaultExitTransition
}
