package ru.livetyping.zarina.core.uikit.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.IntOffset

public fun AnimatedContentTransitionScope<*>.zarinaEnterSlideTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

public fun AnimatedContentTransitionScope<*>.zarinaExitSlideTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

public fun AnimatedContentTransitionScope<*>.zarinaPopEnterSlideTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

public fun AnimatedContentTransitionScope<*>.zarinaPopExitSlideTransition(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec: FiniteAnimationSpec<IntOffset> = DefaultAnimationSpecIntOffset,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = animationSpec,
    )
}

@Suppress("UnusedReceiverParameter")
public fun AnimatedContentTransitionScope<*>.zarinaEnterFadeInTransition(
    animationSpec: FiniteAnimationSpec<Float> = DefaultAnimationSpecFloat,
): EnterTransition {
    return fadeIn(animationSpec)
}

@Suppress("UnusedReceiverParameter")
public fun AnimatedContentTransitionScope<*>.zarinaExitFadeOutTransition(
    animationSpec: FiniteAnimationSpec<Float> = DefaultAnimationSpecFloat,
): ExitTransition {
    return fadeOut(animationSpec)
}

private const val TransitionDurationMillis = 300

private val DefaultAnimationSpecIntOffset: FiniteAnimationSpec<IntOffset> =
    tween(TransitionDurationMillis)

private val DefaultAnimationSpecFloat: FiniteAnimationSpec<Float> =
    tween(TransitionDurationMillis)
