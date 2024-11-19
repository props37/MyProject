package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasAnyRoute
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitFadeOutTransition
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItemNavEntryClasses
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find

fun NavGraphBuilder.homeFeature(
    feature: HomeFeature,
    actions: HomeNavActions,
    features: Features,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                val onboardingNavEntryClass =
                    features.find<OnboardingFeature>().getNavEntryClass()

                val initialDestination = initialState.destination
                when {
                    initialDestination.hasRoute(onboardingNavEntryClass) -> {
                        EnterTransition.None
                    }

                    initialDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            exitTransition = {
                val targetDestination = targetState.destination
                when {
                    targetDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val initialDestination = initialState.destination
                when {
                    initialDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val targetDestination = targetState.destination
                when {
                    targetDestination.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberHomeNavActions(
    features: Features,
    navController: NavHostController
): HomeNavActions {
    return remember(features, navController) {
        HomeNavActions(
            bannerClicked = {
                TODO()
                // TODO: [Top] Implement
            },
        )
    }
}
