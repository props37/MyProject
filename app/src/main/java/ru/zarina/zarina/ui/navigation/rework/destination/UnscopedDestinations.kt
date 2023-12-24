package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.Onboarding) {
        OnboardingScreen(
            navigateForward = { action ->
                // TODO: [High] Handle actions
                navController.navigate(HomeGraph.Home.route) {
                    popUpTo(0)
                }
            },
        )
    }
}

fun NavGraphBuilder.citySelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.CitySelector) {
        CitySelectorBottomSheetScreen()
    }
}
