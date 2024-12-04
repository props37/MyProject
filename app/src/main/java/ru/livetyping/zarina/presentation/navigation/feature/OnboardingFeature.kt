package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.onboardingFeature(
    feature: OnboardingFeature,
    actions: OnboardingNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            exitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()
                when {
                    targetDestinationWithParent.hasRoute(CitySelectorFeature.getNavEntryClass()) -> {
                        ExitTransition.KeepUntilTransitionsFinished
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()
                when {
                    initialDestinationWithParent.hasRoute(CitySelectorFeature.getNavEntryClass()) -> {
                        EnterTransition.None
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberOnboardingNavActions(
    navController: NavHostController
): OnboardingNavActions {
    return remember(navController) {
        OnboardingNavActions(
            onOnboardingCompleted = {
                navController.navigate(HomeFeature.getNavEntry()) {
                    popUpTo(0)
                }
                // TODO: [Top] Show default city dialog?
            },
            onSelectCityClicked = {
                val citySelectorParams = CitySelectorNavParams()
                val citySelectorNavEntry = CitySelectorFeature.getNavEntry(citySelectorParams)
                navController.navigate(citySelectorNavEntry)
            },
        )
    }
}
