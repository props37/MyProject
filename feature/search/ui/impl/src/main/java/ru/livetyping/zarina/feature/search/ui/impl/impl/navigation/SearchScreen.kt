package ru.livetyping.zarina.feature.search.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.SearchNavActions
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.SearchScreen

internal fun NavGraphBuilder.searchScreen(actions: SearchNavActions) {
    composable<SearchFeature.NavEntry.StartNavEntry> {
        SearchScreen(actions)
    }
}
