package ru.livetyping.zarina.presentation.navigation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.HomeGraph
import ru.livetyping.zarina.presentation.screen.onboarding.OnboardingScreen
import ru.livetyping.zarina.presentation.screen.onboarding.OnboardingScreenAction
import ru.livetyping.zarina.presentation.screen.onboarding.OnboardingViewModel

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

                UnscopedDestinations.CitySelector.routeSchema -> {
                    ExitTransition.KeepUntilTransitionsFinished
                }

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.CitySelector.routeSchema -> EnterTransition.None
                else -> null
            }
        }
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
                            navController.navigateToDefaultCityDialog()
                        }
                    }

                    is OnboardingScreenAction.SelectCityClicked -> {
                        navController.navigateToCitySelectorScreen(action.currentCity)
                    }
                }
            },
        )
    }
}
