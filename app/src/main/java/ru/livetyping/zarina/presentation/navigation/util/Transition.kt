package ru.livetyping.zarina.presentation.navigation.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

@Suppress("UnusedReceiverParameter")
fun AnimatedContentTransitionScope<NavBackStackEntry>.fadeInTransition(
    animationSpec: FiniteAnimationSpec<Float> = DefaultAnimationSpecFloat,
): EnterTransition {
    return fadeIn(animationSpec)
}

@Suppress("UnusedReceiverParameter")
fun AnimatedContentTransitionScope<NavBackStackEntry>.fadeOutTransition(
    animationSpec: FiniteAnimationSpec<Float> = DefaultAnimationSpecFloat,
): ExitTransition {
    return fadeOut(animationSpec)
}

private val DefaultAnimationSpecFloat: FiniteAnimationSpec<Float>
    get() = tween(NavigationTransitionDurationMillis)

const val NavigationTransitionDurationMillis = 300
