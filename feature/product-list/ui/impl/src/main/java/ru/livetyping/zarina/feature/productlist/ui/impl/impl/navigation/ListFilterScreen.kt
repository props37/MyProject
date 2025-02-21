package ru.livetyping.zarina.feature.productlist.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter.ListFilterNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter.ListFilterNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter.ListFilterScreen

internal fun NavGraphBuilder.listFilterScreen(actions: ListFilterNavActions) {
    composable<ListFilterNavEntry>(typeMap = ListFilterNavEntry.typeMap()) {
        ListFilterScreen(actions)
    }
}
