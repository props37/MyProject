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
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.OnboardingScreen
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.OnboardingViewModel

public class OnboardingFeatureImpl : OnboardingFeature {
    override fun NavGraphBuilder.composable(
        actions: OnboardingFeature.NavActions,
        resultRetrievers: OnboardingFeature.NavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        composable<OnboardingFeature.NavEntry>(
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
                        resultRetrievers.selectedCityResultRetriever.get(navBackStackEntry)
                    factory.create(selectedCityResult)
                },
            )
        }
    }
}
