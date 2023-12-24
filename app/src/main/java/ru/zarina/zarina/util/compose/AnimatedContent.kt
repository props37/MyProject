package ru.zarina.zarina.util.compose

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith

val AnimatedContentDefaultTransitionSpec = {
    fadeIn(
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    ) togetherWith fadeOut(animationSpec = tween(durationMillis = 90))
}
