package ru.zarina.zarina.ui.screens.favourites

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    favorites: LazyPagingItems<Product>,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.favourites)
                // TODO elevation on scroll
            )
        }
    ) {

    }
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
