package ru.livetyping.zarina.presentation.navigation.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideEnterTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideExitTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopEnterTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopExitTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

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

private val DefaultAnimationSpecIntOffset: FiniteAnimationSpec<IntOffset>
    get() = tween(NavigationTransitionDurationMillis)

private val DefaultAnimationSpecFloat: FiniteAnimationSpec<Float>
    get() = tween(NavigationTransitionDurationMillis)

const val NavigationTransitionDurationMillis = 300
