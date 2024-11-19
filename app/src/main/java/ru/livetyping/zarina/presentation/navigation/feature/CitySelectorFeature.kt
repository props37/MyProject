package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopExitSlideTransition
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find

fun NavGraphBuilder.citySelectorFeature(
    feature: CitySelectorFeature,
    actions: CitySelectorNavActions,
    features: Features,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                val onboardingNavEntryClass =
                    features.find<OnboardingFeature>().getNavEntryClass()
                when {
                    initialState.destination.hasRoute(onboardingNavEntryClass) -> {
                        zarinaEnterSlideTransition(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        )
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val onboardingNavEntryClass =
                    features.find<OnboardingFeature>().getNavEntryClass()
                when {
                    targetState.destination.hasRoute(onboardingNavEntryClass) -> {
                        zarinaPopExitSlideTransition(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        )
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberCitySelectorNavActions(
    features: Features,
    navController: NavHostController
): CitySelectorNavActions {
    return remember(features, navController) {
        CitySelectorNavActions()
    }
}
