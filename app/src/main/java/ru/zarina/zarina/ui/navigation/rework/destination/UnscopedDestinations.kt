package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destinations.CommonDestinations
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(CommonDestinations.Onboarding) {
        OnboardingScreen()
    }
}
