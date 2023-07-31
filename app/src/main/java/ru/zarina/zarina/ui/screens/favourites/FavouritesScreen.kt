package ru.zarina.zarina.ui.screens.favourites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState

@Composable
fun FavoritesScreenContent() {

}

@Composable
fun FavoritesScreen() {
    val viewModel = koinViewModel<FavoritesViewModel>()

    FavoritesScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    FavoritesScreenContent()
}

@Composable
fun FavoritesScreenBehavior(
    sideEffects: Flow<FavoritesViewModel.SideEffect>,
) {
    NavigationBarState(isVisible = true, isAnimated = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}
