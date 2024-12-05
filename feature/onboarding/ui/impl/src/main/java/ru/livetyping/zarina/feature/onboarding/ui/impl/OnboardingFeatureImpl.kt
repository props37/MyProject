package ru.livetyping.zarina.feature.onboarding.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavEntry
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavResultRetrievers
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.OnboardingScreen
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.OnboardingViewModel

public class OnboardingFeatureImpl : OnboardingFeature {
    override fun NavGraphBuilder.composable(
        actions: OnboardingNavActions,
        resultProvider: OnboardingNavResultRetrievers,
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
        ) { navBackStackEntry ->
            OnboardingScreen(
                navActions = actions,
                viewModel = hiltViewModel { factory: OnboardingViewModel.Factory ->
                    val selectedCityResult =
                        resultProvider.selectedCityResultRetriever.get(navBackStackEntry)
                    factory.create(selectedCityResult)
                },
            )
        }
    }
}
