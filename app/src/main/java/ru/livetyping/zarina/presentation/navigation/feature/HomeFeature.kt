package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigationutil.hasAnyRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitFadeOutTransition
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItemNavEntryClasses
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.homeFeature(
    feature: HomeFeature,
    actions: HomeNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                val onboardingNavEntryClass = OnboardingFeature.getNavEntryClass()
                val initialDestinationWithParent = initialDestination.withParent()

                when {
                    initialDestination.hasRoute(onboardingNavEntryClass) -> {
                        EnterTransition.None
                    }

                    initialDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            exitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()

                when {
                    targetDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaExitFadeOutTransition()
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()

                when {
                    initialDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
                        zarinaEnterFadeInTransition()
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()

                when {
                    targetDestinationWithParent.hasAnyRoute(BottomNavBarItemNavEntryClasses) -> {
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
    navController: NavHostController
): HomeNavActions {
    return remember(navController) {
        HomeNavActions(
            bannerClicked = {
                TODO()
                // TODO: [Top] Implement
            },
        )
    }
}
