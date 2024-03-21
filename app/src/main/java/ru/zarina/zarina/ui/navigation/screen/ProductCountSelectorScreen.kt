package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorScreenAction

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
