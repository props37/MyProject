package ru.zarina.zarina.ui.screens.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ModalError
import ru.zarina.zarina.ui.common.components.ProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.bottomNavigationPadding
import ru.zarina.zarina.ui.common.components.color.ColorPickerDefaults
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    favorites: LazyPagingItems<Product>,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    shakingFavorites: ImmutableSet<Product.Id>,
) {
    val productListState = rememberLazyGridState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.favorites),
                isElevated = productListState.canScrollBackward,
            )
        }
    ) {
        val isLoading =
            favorites.loadState.append == LoadState.Loading || favorites.loadState.refresh == LoadState.Loading
        val errorState = when {
            !isLoading && favorites.itemCount == 0 -> ErrorState(
                icon = R.drawable.ic_heart_96,
                title = Text.Resource(R.string.favorites_are_empty),
                subtitle = Text.Resource(R.string.save_interesting_products_to_favorites),
                isButtonVisible = false,
                buttonText = null
            )

            else -> null
        }
        if (errorState == null) {
            val colorPickerDimensions = ColorPickerDefaults.tinyDimensions()

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = productListState,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                modifier = Modifier
                    .fillMaxSize()
                    .bottomNavigationPadding()
            ) {

                items(
                    count = favorites.itemCount,
                    key = favorites.itemKey { it.id.value },
                    contentType = favorites.itemContentType { null }
                ) { productIndex ->
                    val product = favorites[productIndex]
                    if (product != null)
                        ProductCard(
                            product = product,
                            isMediaScrollable = true,
                            onClick = { onProductClick(product) },
                            isFavoriteShaking = shakingFavorites.contains(product.id),
                            onFavoriteChange = { onFavoriteChange(product, it) },
                            colorPickerDimensions = colorPickerDimensions,
                        )
                }
            }
        } else {
            ModalError(
                state = errorState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(UiKitTheme.colorsOld.screenBackground)
                    .navigationBarsPadding()
                    .bottomNavigationPadding()
            )
        }
    }
}

@Composable
fun FavoritesScreen(
    showProduct: (Product) -> Unit,
) {
    val viewModel = koinViewModel<FavoritesViewModel>()

    val favorites = viewModel.favorites.collectAsLazyPagingItems()
    val shakingFavorites by viewModel.shakingFavorites.collectAsStateWithLifecycle()

    FavoritesScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
    )

    FavoritesScreenContent(
        favorites = favorites,
        onProductClick = remember { { viewModel.onProductClick(it) } },
        onFavoriteChange = remember {
            { product, isFavorite ->
                viewModel.onFavoriteChange(product, isFavorite)
            }
        },
        shakingFavorites = shakingFavorites
    )
}

@Composable
fun FavoritesScreenBehavior(
    sideEffects: Flow<FavoritesViewModel.SideEffect>,
    showProduct: (Product) -> Unit,
) {
    NavigationBarState(isVisible = true, isAnimated = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is FavoritesViewModel.SideEffect.ShowProduct -> showProduct(effect.product)
            }
        }
    }
}
