package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopExitSlideTransition
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.citySelectorFeature(
    feature: CitySelectorFeature,
    actions: CitySelectorNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()
                when {
                    initialDestinationWithParent.hasRoute(OnboardingFeature.getNavEntryClass()) -> {
                        zarinaEnterSlideTransition(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        )
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()
                when {
                    targetDestinationWithParent.hasRoute(OnboardingFeature.getNavEntryClass()) -> {
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
    navController: NavHostController
): CitySelectorNavActions {
    return remember(navController) {
        CitySelectorNavActions(
            backClicked = { navController.navigateUp() },
            citySelected = { city ->
                navController.navigateUp()
                // TODO: [Top] Implement
                TODO()
            },
        )
    }
}
