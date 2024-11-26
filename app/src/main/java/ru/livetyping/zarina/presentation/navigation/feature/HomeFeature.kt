package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterFadeInTransition
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.presentation.navigation.util.initialDestination

fun NavGraphBuilder.homeFeature(
    feature: HomeFeature,
    actions: HomeNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            enterTransition = {
                when {
                    initialDestination.hasRoute(OnboardingFeature.getNavEntryClass()) -> {
                        zarinaEnterFadeInTransition()
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
