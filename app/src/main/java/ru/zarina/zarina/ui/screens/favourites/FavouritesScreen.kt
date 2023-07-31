package ru.zarina.zarina.ui.screens.favourites

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ProductRowCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.bottomNavigationPaddingValues
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    favorites: LazyPagingItems<Product>,
) {
    val productListState = rememberLazyListState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.favourites),
                isElevated = productListState.canScrollBackward,
            )
        }
    ) {
        LazyColumn(
            state = productListState,
            contentPadding = bottomNavigationPaddingValues() + WindowInsets.navigationBars.asPaddingValues(),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                count = favorites.itemCount,
                key = favorites.itemKey { it.id.value },
                contentType = favorites.itemContentType { null }
            ) { index ->
                Divider(
                    thickness = 1.dp,
                    color = UiKitTheme.colors.listDivider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                val product = favorites[index]
                if (product != null)
                    ProductRowCard(
                        product = product,
                        onClick = {}, // TODO
                        onFavoriteChange = {}, // TODO
                    )
            }
        }
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
