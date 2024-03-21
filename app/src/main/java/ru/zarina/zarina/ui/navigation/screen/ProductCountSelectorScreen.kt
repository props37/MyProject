package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorBottomSheetScreen

fun NavGraphBuilder.productCountSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(CartGraph.ProductCountSelector) {
        ProductCountSelectorBottomSheetScreen()
    }
}
