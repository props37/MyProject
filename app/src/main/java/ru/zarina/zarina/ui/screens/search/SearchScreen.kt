package ru.zarina.zarina.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreenContent() {

}

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchViewModel>()

    SearchScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    SearchScreenContent()
}

@Composable
fun SearchScreenBehavior(
    sideEffects: Flow<SearchViewModel.SideEffect>,
) {

    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}
