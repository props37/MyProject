package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.NavigationTransitionDurationMillis
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.products.ProductsScreen
import ru.zarina.zarina.ui.screen.products.ProductsScreenAction
import ru.zarina.zarina.ui.screen.products.ProductsViewModel

fun NavGraphBuilder.productsScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Products,
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Filters.routeSchema -> {
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
                UnscopedDestinations.Filters.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
    ) {
        ProductsScreen(
            viewModel = hiltViewModel { factory: ProductsViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigateForward = { action ->
                when (action) {
                    is ProductsScreenAction.FiltersClicked -> {
                        val args = UnscopedDestinations.Filters.Args(
                            categoryId = action.categoryId,
                            filters = action.filters,
                        )
                        val route = UnscopedDestinations.Filters.createRoute(args)
                        navController.navigate(route)
                    }
                }
            },
            navigateBackward = {
                navController.popBackStack(
                    route = UnscopedDestinations.Products.routeSchema,
                    inclusive = true,
                )
            },
        )
    }
}
