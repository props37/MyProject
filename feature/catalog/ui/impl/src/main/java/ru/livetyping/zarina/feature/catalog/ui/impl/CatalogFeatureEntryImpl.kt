package ru.livetyping.zarina.feature.catalog.ui.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeatureEntry
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavEntry
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.CatalogScreen

public class CatalogFeatureEntryImpl : CatalogFeatureEntry {
    override fun NavGraphBuilder.composable(actions: CatalogNavActions) {
        composable<CatalogNavEntry> {
            CatalogScreen(navActions = actions)
        }
    }
}
