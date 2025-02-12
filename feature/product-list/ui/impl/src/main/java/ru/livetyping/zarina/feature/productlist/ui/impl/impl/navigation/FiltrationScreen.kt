package ru.livetyping.zarina.feature.productlist.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationScreen

internal fun NavGraphBuilder.filtrationScreen(actions: FiltrationNavActions) {
    composable<FiltrationNavEntry>(
        typeMap = FiltrationNavEntry.typeMap(),
    ) {
        FiltrationScreen(actions)
    }
}
