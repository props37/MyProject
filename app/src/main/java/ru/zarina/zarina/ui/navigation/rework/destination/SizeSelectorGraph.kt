package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.heightSelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.destination.unscoped.sizeSelectorBottomSheetScreen
import ru.zarina.zarina.ui.navigation.rework.graph.SizeSelectorGraph

fun NavGraphBuilder.sizeSelectorGraph(navController: NavHostController) {
    navigationGraph(SizeSelectorGraph) {
        sizeSelectorBottomSheetScreen(navController)
        heightSelectorBottomSheetScreen(navController)
    }
}
