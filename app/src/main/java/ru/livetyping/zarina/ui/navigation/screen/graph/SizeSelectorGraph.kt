package ru.livetyping.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.ui.navigation.base.navigationGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.ui.navigation.screen.heightSelectorBottomSheetScreen
import ru.livetyping.zarina.ui.navigation.screen.sizeSelectorBottomSheetScreen
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.sizeSelectorGraph(navController: NavHostController) {
    navigationGraph(SizeSelectorGraph) {
        sizeSelectorBottomSheetScreen(navController)
        heightSelectorBottomSheetScreen(navController)
    }
}

fun NavHostController.navigateToSizeSelectorGraph(product: Product) {
    val args = SizeSelectorGraph.SizeSelector.Args(product)
    this.navigate(
        route = SizeSelectorGraph.routeSchema,
        args = SizeSelectorGraph.createArgsBundle(args),
    )
}
