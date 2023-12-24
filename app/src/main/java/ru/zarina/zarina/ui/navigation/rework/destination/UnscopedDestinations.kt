package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.Onboarding) {
        OnboardingScreen(
            navigateForward = {
                navController.navigate(HomeGraph.Home.route) {
                    popUpTo(0)
                }
            },
        )
    }
}
