package ru.livetyping.zarina.presentation.navigation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorScreen
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.citySelectorBottomSheetScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.CitySelector,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Onboarding.routeSchema -> {
                    slideEnterTransition(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    )
                }

                CartGraph.Cart.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Onboarding.routeSchema -> {
                    slidePopExitTransition(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    )
                }

                CartGraph.Cart.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        CitySelectorScreen(
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

fun NavHostController.navigateToCitySelectorScreen(
    currentCity: City? = null,
    title: Text? = null,
) {
    val args = UnscopedDestinations.CitySelector.Args(currentCity = currentCity, title = title)
    this.navigate(
        route = UnscopedDestinations.CitySelector.routeSchema,
        args = UnscopedDestinations.CitySelector.createArgsBundle(args),
    )
}
