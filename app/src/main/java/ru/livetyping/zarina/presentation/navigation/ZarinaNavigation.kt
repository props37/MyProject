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
import ru.livetyping.zarina.feature.home.ui.HomeFeatureEntry
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeatureEntry
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

    val onboardingFeature = features.find<OnboardingFeatureEntry>()
    val onboardingNavActions = remember { OnboardingNavActions() }

    val homeFeature = features.find<HomeFeatureEntry>()
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

        with(homeFeature) {
            composable(homeNavActions)
        }
    }
}
