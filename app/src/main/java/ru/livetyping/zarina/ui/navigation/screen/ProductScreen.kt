package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.product.ProductScreen
import ru.livetyping.zarina.ui.screen.product.ProductScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Product,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ProductScreen(
            navigate = { action ->
                when (action) {
                    ProductScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Product.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            }
        )
    }
}

fun NavHostController.navigateToProductScreen(productId: Product.Id) {
    val args = UnscopedDestinations.Product.Args(productId)
    this.navigate(
        route = UnscopedDestinations.Product.routeSchema,
        args = UnscopedDestinations.Product.createArgsBundle(args),
    )
}
