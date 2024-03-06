package ru.zarina.zarina.ui.navigation.rework.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.bottomnavbar.BottomNavBarItem
import ru.zarina.zarina.ui.bottomnavbar.navigateToBottomNavBarItem
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.NavigationTransitionDurationMillis
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.CartGraph
import ru.zarina.zarina.ui.navigation.rework.destination.graph.CatalogGraph
import ru.zarina.zarina.ui.navigation.rework.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.cart.CartScreen
import ru.zarina.zarina.ui.screen.cart.CartScreenAction
import ru.zarina.zarina.util.library.navigation.navigate

fun NavGraphBuilder.cartScreen(navController: NavHostController) {
    composableDestination(
        destination = CartGraph.Cart,
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.CitySelector.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.CitySelector.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
    ) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        CartScreen(
            navigate = { action ->
                when (action) {
                    CartScreenAction.GoToCatalogClicked -> {
                        navController.navigateToBottomNavBarItem(BottomNavBarItem.Catalog)
                        navController.popBackStack(
                            route = CatalogGraph.Catalog.routeSchema,
                            inclusive = false,
                        )
                    }

                    is CartScreenAction.CityClicked -> {
                        val args = UnscopedDestinations.CitySelector.Args(
                            city = action.city,
                            title = Text.Resource(R.string.city_change),
                        )
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
