package ru.livetyping.zarina.feature.search.ui.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.search.ui.impl.filtration.FiltrationNavActions
import ru.livetyping.zarina.feature.search.ui.impl.filtration.FiltrationNavEntry
import ru.livetyping.zarina.feature.search.ui.impl.filtration.FiltrationScreen
import ru.livetyping.zarina.feature.search.ui.impl.filtration.FiltrationViewModel
import ru.livetyping.zarina.feature.search.ui.impl.listfilter.ListFilterResult

internal fun NavGraphBuilder.filtrationScreen(actions: FiltrationNavActions) {
    composable<FiltrationNavEntry>(
        typeMap = FiltrationNavEntry.typeMap(),
    ) { navBackStackEntry ->
        FiltrationScreen(
            navActions = actions,
            viewModel = hiltViewModel { factory: FiltrationViewModel.Factory ->
                val listFilterResultFlow = navBackStackEntry.savedStateHandle
                    .getStateFlow<ListFilterResult?>(
                        key = ListFilterResult.KEY,
                        initialValue = null,
                    )
                factory.create(listFilterResultFlow)
            },
        )
    }
}
