package ru.livetyping.zarina.feature.product.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.SizeTableNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.SizeTableNavEntry
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.SizeTableScreen

internal fun NavGraphBuilder.sizeTableScreen(actions: SizeTableNavActions) {
    composable<SizeTableNavEntry>(typeMap = SizeTableNavEntry.typeMap()) {
        SizeTableScreen(actions)
    }
}
