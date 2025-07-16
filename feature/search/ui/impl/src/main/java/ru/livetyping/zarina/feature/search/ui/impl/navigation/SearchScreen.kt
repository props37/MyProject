package ru.livetyping.zarina.feature.search.ui.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.feature.search.ui.impl.filtration.FiltrationResult
import ru.livetyping.zarina.feature.search.ui.impl.search.SearchNavActions
import ru.livetyping.zarina.feature.search.ui.impl.search.SearchScreen
import ru.livetyping.zarina.feature.search.ui.impl.search.SearchViewModel

internal fun NavGraphBuilder.searchScreen(actions: SearchNavActions) {
    composable<SearchFeature.NavEntry.StartNavEntry> { navBackStackEntry ->
        SearchScreen(
            navActions = actions,
            viewModel = hiltViewModel { factory: SearchViewModel.Factory ->
                val filtrationResultFlow = navBackStackEntry.savedStateHandle
                    .getStateFlow<FiltrationResult?>(
                        key = FiltrationResult.KEY,
                        initialValue = null,
                    )
                factory.create(filtrationResultFlow)
            },
        )
    }
}
