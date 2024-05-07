package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.navigation.screen.cartScreen
import ru.livetyping.zarina.presentation.navigation.screen.productCountSelectorBottomSheetScreen

fun NavGraphBuilder.cartGraph(navController: NavHostController) {
    navigationGraph(CartGraph) {
        cartScreen(navController)
        productCountSelectorBottomSheetScreen(navController)
    }
}
