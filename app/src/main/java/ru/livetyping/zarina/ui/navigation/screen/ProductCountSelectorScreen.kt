package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.ui.navigation.base.bottomSheetDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.ui.screen.productcountselector.ProductCountSelectorBottomSheetScreen
import ru.livetyping.zarina.ui.screen.productcountselector.ProductCountSelectorScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productCountSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(CartGraph.ProductCountSelector) {
        ProductCountSelectorBottomSheetScreen(
            navigate = { action ->
                when (action) {
                    ProductCountSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CartGraph.ProductCountSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    ProductCountSelectorScreenAction.CountChanged -> {
                        navController.popBackStack(
                            route = CartGraph.ProductCountSelector.routeSchema,
                            inclusive = true,
                        )
                        val result = CartGraph.ProductCountSelector.Result(countChanged = true)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(CartGraph.ProductCountSelector.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToProductCountSelector(
    productId: Product.Id,
    barcode: Barcode,
    initialCount: Int,
    availableCount: Int,
    deliveryType: DeliveryType,
) {
    val args = CartGraph.ProductCountSelector.Args(
        productId = productId,
        barcode = barcode,
        initialCount = initialCount,
        availableCount = availableCount,
        deliveryType = deliveryType,
    )
    this.navigate(
        route = CartGraph.ProductCountSelector.routeSchema,
        args = CartGraph.ProductCountSelector.createArgsBundle(args),
    )
}
