package ru.livetyping.zarina.feature.search.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.search.ui.impl.impl.filtration.FiltrationNavActions
import ru.livetyping.zarina.feature.search.ui.impl.impl.filtration.FiltrationNavEntry
import ru.livetyping.zarina.feature.search.ui.impl.impl.filtration.FiltrationScreen
import ru.livetyping.zarina.feature.search.ui.impl.impl.filtration.FiltrationViewModel
import ru.livetyping.zarina.feature.search.ui.impl.impl.listfilter.ListFilterResult

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
