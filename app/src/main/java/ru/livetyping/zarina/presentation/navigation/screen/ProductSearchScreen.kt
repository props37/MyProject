package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreen
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenAction

fun NavGraphBuilder.productSearchScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSearch,
    ) {
        ProductSearchScreen(
            navigate = { action ->
                when (action) {
                    ProductSearchScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSearch.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
