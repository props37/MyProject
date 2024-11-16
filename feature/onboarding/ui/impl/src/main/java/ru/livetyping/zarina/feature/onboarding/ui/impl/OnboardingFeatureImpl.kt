package ru.livetyping.zarina.feature.onboarding.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavEntry
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.OnboardingScreen
import kotlin.reflect.KClass

public class OnboardingFeatureImpl : OnboardingFeature {
    override fun NavGraphBuilder.composable(
        actions: OnboardingNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        composable<OnboardingNavEntry>(
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            OnboardingScreen(navActions = actions)
        }
    }

    override fun getNavEntry(params: Unit): OnboardingNavEntry = OnboardingNavEntry

    override fun getNavEntryClass(): KClass<OnboardingNavEntry> = OnboardingNavEntry::class
}
