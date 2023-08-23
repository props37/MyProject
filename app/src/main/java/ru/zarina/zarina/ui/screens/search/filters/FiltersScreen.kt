package ru.zarina.zarina.ui.screens.search.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.ui.screens.catalog.filters.FilterType

@Composable
fun FiltersScreenContent() {

}

@Composable
fun FiltersScreen(
    savedStateHandle: SavedStateHandle,
    searchSavedStateHandle: SavedStateHandle,
    showFilter: (FilterType) -> Unit,
    goBack: () -> Unit,
) {
    val viewModel =
        koinViewModel<FiltersViewModel> { parametersOf(savedStateHandle, searchSavedStateHandle) }

    FiltersScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    FiltersScreenContent()
}

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}
