package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.home.ui.HomeNavEntry
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find

fun NavGraphBuilder.onboardingFeature(
    feature: OnboardingFeature,
    actions: OnboardingNavActions,
    features: Features,
) {
    with(feature) {
        composable(
            actions = actions,
            exitTransition = {
                val citySelectorNavEntryClass =
                    features.find<CitySelectorFeature>().getNavEntryClass()
                when {
                    targetState.destination.hasRoute(citySelectorNavEntryClass) -> {
                        ExitTransition.KeepUntilTransitionsFinished
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val citySelectorNavEntryClass =
                    features.find<CitySelectorFeature>().getNavEntryClass()
                when {
                    initialState.destination.hasRoute(citySelectorNavEntryClass) -> {
                        EnterTransition.None
                    }

                    else -> null
                }
            }
        )
    }
}

@Composable
fun rememberOnboardingNavActions(
    features: Features,
    navController: NavHostController
): OnboardingNavActions {
    return remember(features, navController) {
        OnboardingNavActions(
            onboardingCompleted = {
                navController.navigate(HomeNavEntry) {
                    popUpTo(0)
                }
                // TODO: [Top] Show default city dialog?
            },
            selectCityClicked = {
                val citySelectorParams = CitySelectorNavParams()
                val citySelectorNavEntry = features.find<CitySelectorFeature>()
                    .getNavEntry(citySelectorParams)
                navController.navigate(citySelectorNavEntry)
            },
        )
    }
}
