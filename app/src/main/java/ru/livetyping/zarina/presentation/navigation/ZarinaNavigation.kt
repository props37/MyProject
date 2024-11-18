package ru.livetyping.zarina.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaExitSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopExitSlideTransition
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.home.ui.HomeNavEntry
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavEntry
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.feature.find
import ru.livetyping.zarina.presentation.navigation.base.Destination

@Composable
fun ZarinaNavigation(
    features: Features,
    navController: NavHostController,
    startDestination: Destination<Unit>,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    val navController by rememberUpdatedState(navController)

    // TODO: [Top] Refactor
    val onboardingFeature = features.find<OnboardingFeature>()
    val onboardingNavActions = remember(navController) {
        OnboardingNavActions(
            onboardingCompleted = {
                navController.navigate(HomeNavEntry) {
                    popUpTo(0)
                }
                // TODO: [Top] Show default city dialog?
            },
            selectCityClicked = {
                val citySelectorParams = CitySelectorNavParams()
                val navEntry = features.find<CitySelectorFeature>().getNavEntry(citySelectorParams)
                navController.navigate(navEntry)
            },
        )
    }

    val citySelectorFeature = features.find<CitySelectorFeature>()

    val homeFeature = features.find<HomeFeature>()
    val homeNavActions = remember {
        // TODO: [Top] Implement
        HomeNavActions(
            bannerClicked = {},
        )
    }

    NavHost(
        navController = navController,
        startDestination = OnboardingNavEntry, // TODO: [Top] Implement
        enterTransition = { zarinaEnterSlideTransition() },
        exitTransition = { zarinaExitSlideTransition() },
        popEnterTransition = { zarinaPopEnterSlideTransition() },
        popExitTransition = { zarinaPopExitSlideTransition() },
        modifier = modifier,
    ) {
        with(onboardingFeature) {
            composable(onboardingNavActions)
        }

        with(citySelectorFeature) {
            composable(CitySelectorNavActions())
        }

        with(homeFeature) {
            composable(homeNavActions)
        }
    }
}
