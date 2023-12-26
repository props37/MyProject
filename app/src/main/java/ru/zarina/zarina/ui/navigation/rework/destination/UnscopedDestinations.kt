package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenResult
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenAction

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.Onboarding) {
        OnboardingScreen(
            navigateForward = { action ->
                when (action) {
                    is OnboardingScreenAction.OnboardingCompleted -> {
                        navController.navigate(HomeGraph.Home.route) {
                            popUpTo(0)
                        }
                    }

                    is OnboardingScreenAction.SelectCityClicked -> {
                        val args = UnscopedDestinations.CitySelector.Args(action.currentCity)
                        val route = UnscopedDestinations.CitySelector.createRoute(args)
                        navController.navigate(route)
                    }
                }
            },
        )
    }
}

fun NavGraphBuilder.citySelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.CitySelector) {
        CitySelectorBottomSheetScreen(
            navigateBackward = { result ->
                when (result) {
                    CitySelectorScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
