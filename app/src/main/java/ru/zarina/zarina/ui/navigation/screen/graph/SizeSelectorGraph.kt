package ru.zarina.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.navigation.screen.heightSelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.screen.sizeSelectorBottomSheetScreen

fun NavGraphBuilder.sizeSelectorGraph(navController: NavHostController) {
    navigationGraph(SizeSelectorGraph) {
        sizeSelectorBottomSheetScreen(navController)
        heightSelectorBottomSheetScreen(navController)
    }
}
