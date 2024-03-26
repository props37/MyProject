package ru.zarina.zarina.ui.navigation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.navigation.util.slideEnterTransition
import ru.zarina.zarina.ui.navigation.util.slidePopExitTransition
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreen
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenAction
import ru.zarina.zarina.util.library.navigation.navigate

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
    city: City? = null,
    title: Text? = null,
) {
    val args = UnscopedDestinations.CitySelector.Args(city = city, title = title)
    this.navigate(
        route = UnscopedDestinations.CitySelector.routeSchema,
        args = UnscopedDestinations.CitySelector.createArgsBundle(args),
    )
}
