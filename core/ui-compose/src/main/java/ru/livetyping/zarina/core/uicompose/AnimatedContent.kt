package ru.livetyping.zarina.core.uicompose

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Stable

@Stable
public val AnimatedContentDefaultEnterTransition: EnterTransition =
    fadeIn(
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
    )

@Stable
public val AnimatedContentDefaultExitTransition: ExitTransition =
    fadeOut(animationSpec = tween(durationMillis = 90))

@Stable
public val AnimatedContentDefaultTransitionSpec: ContentTransform =
    AnimatedContentDefaultEnterTransition togetherWith AnimatedContentDefaultExitTransition

@Stable
public val AnimatedContentCrossfadeTransitionSpec: ContentTransform =
    fadeIn(AnimatedContentCrossfadeAnimSpec) togetherWith fadeOut(AnimatedContentCrossfadeAnimSpec)

private val AnimatedContentCrossfadeAnimSpec: TweenSpec<Float>
    get() = tween()
