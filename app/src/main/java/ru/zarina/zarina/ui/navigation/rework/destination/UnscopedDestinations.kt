package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenResult
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenAction
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.Onboarding) {
        OnboardingScreen(
            viewModel = hiltViewModel { factory : OnboardingViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigateForward = { action ->
                when (action) {
                    is OnboardingScreenAction.OnboardingCompleted -> {
                        navController.navigate(HomeGraph.route) {
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

                    is CitySelectorScreenResult.CitySelected -> {
                        val cityParcelable = CityParcelable.fromCity(result.city)
                        val result = UnscopedDestinations.CitySelector.Result(cityParcelable)
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.CitySelector.RESULT_KEY, result)
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
