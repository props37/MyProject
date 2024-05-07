package ru.livetyping.zarina.presentation.navigation.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

const val NavigationTransitionDurationMillis = 300

private val DefaultAnimationSpec: FiniteAnimationSpec<IntOffset>
    get() = tween(NavigationTransitionDurationMillis)

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideEnterTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpec,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideExitTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpec,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopEnterTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpec,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopExitTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpec,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}
