package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizetable.SizeTableBottomSheetScreen

fun NavGraphBuilder.sizeTableBottomSheetScreen() {
    bottomSheetDestination(UnscopedDestinations.SizeTable) {
        SizeTableBottomSheetScreen()
    }
}
