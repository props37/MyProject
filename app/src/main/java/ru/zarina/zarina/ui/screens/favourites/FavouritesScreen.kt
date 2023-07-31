package ru.zarina.zarina.ui.screens.favourites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState

@Composable
fun FavoritesScreenContent(
    favorites: LazyPagingItems<Product>,
) {

}

@Composable
fun FavoritesScreen() {
    val viewModel = koinViewModel<FavoritesViewModel>()

    val favorites = viewModel.favorites.collectAsLazyPagingItems()

    FavoritesScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    FavoritesScreenContent(
        favorites = favorites,
    )
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
