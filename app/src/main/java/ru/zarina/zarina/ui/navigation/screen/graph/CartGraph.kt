package ru.zarina.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.navigation.screen.cartScreen
import ru.zarina.zarina.ui.navigation.screen.productCountSelectorBottomSheetScreen

fun NavGraphBuilder.cartGraph(navController: NavHostController) {
    navigationGraph(CartGraph) {
        cartScreen(navController)
        productCountSelectorBottomSheetScreen(navController)
    }
}
