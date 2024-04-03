package ru.livetyping.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.navigationGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.ui.navigation.screen.cartScreen
import ru.livetyping.zarina.ui.navigation.screen.productCountSelectorBottomSheetScreen

fun NavGraphBuilder.cartGraph(navController: NavHostController) {
    navigationGraph(CartGraph) {
        cartScreen(navController)
        productCountSelectorBottomSheetScreen(navController)
    }
}
