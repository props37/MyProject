package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorBottomSheetScreenScreen

fun NavGraphBuilder.heightSelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.HeightSelector) {
        HeightSelectorBottomSheetScreenScreen()
    }
}
