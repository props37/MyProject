package ru.zarina.zarina.ui.navigation.rework.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.dialogDestination
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.HomeGraph
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreen
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreenAction
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenAction
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel
import ru.zarina.zarina.util.library.navigation.navigate

// TODO: [Low] Extract to separate files

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
                        navController.navigate(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            args = UnscopedDestinations.CitySelector.createArgsBundle(args),
                        )
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
