package ru.zarina.zarina.ui.screens.search.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun FiltersScreenContent() {

}

@Composable
fun FiltersScreen() {
    val viewModel = koinViewModel<FiltersViewModel>()

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
