package ru.zarina.zarina.ui.navigation.rework.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.dialogDestination
import ru.zarina.zarina.ui.navigation.rework.destination.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenAction
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreen
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreenAction
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenAction
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Onboarding,
        exitTransition = {
            when (targetState.destination.route) {
                HomeGraph.Home.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(durationMillis = 300),
                    )
                }

                else -> null
            }
        },
    ) {
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

                        if (action.userCity == null) {
                            navController.navigate(UnscopedDestinations.DefaultCityDialog.route)
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
            navigate = { action ->
                when (action) {
                    CitySelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CitySelectorScreenAction.CitySelected -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                        val cityParcelable = CityParcelable.from(action.city)
                        val result = UnscopedDestinations.CitySelector.Result(cityParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.CitySelector.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}

fun NavGraphBuilder.defaultCityDialogScreen(navController: NavHostController) {
    dialogDestination(
        destination = UnscopedDestinations.DefaultCityDialog,
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        DefaultCityDialogScreen(
            navigate = { action ->
                when (action) {
                    DefaultCityDialogScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.DefaultCityDialog.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            }
        )
    }
}
