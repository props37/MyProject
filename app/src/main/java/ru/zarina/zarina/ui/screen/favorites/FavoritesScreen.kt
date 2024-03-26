package ru.zarina.zarina.ui.screen.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.component.ProductGrid
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.favorites.FavoritesScreenComponents.FavoriteProductsNotFoundPlaceholder
import ru.zarina.zarina.ui.screen.favorites.FavoritesScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun FavoritesScreen(
    navigate: (FavoritesScreenAction) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    ScreenContent(
        productPagingDataFlow = viewModel.productPagingDataFlow,
        onProductClicked = viewModel::onProductClicked,
        onAddProductToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
        onAddProductToCartClicked = viewModel::onAddProductToCartClicked,
        onSubscribeToProductClicked = viewModel::onSubscribeToProductClicked,
        onClearFavoritesClicked = viewModel::onClearFavoritesClicked,
        onGoToCatalogClicked = viewModel::onGoToCatalogClicked,
        onScreenCreated = viewModel::onScreenCreated,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    productPagingDataFlow: Flow<PagingData<Product>>,
    onProductClicked: (Product) -> Unit,
    onAddProductToFavoritesClicked: (Product) -> Unit,
    onAddProductToCartClicked: (Product) -> Unit,
    onSubscribeToProductClicked: (Product) -> Unit,
    onClearFavoritesClicked: () -> Unit,
    onGoToCatalogClicked: () -> Unit,
    onScreenCreated: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (FavoritesScreenAction) -> Unit,
) {
    FavoritesScreenBehavior(
        onScreenCreated = onScreenCreated,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(
            isClearButtonVisible = true, // TODO: [High] Implement
            onClearClicked = onClearFavoritesClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        ProductGrid(
            productPagingDataFlow = productPagingDataFlow,
            onProductClicked = onProductClicked,
            onAddToFavoritesClicked = onAddProductToFavoritesClicked,
            onAddToCartClicked = onAddProductToCartClicked,
            onSubscribeClicked = onSubscribeToProductClicked,
            onRefreshProducts = {}, // No need for additional logic
            onProductsErrorRefreshClicked = {}, // No need for additional logic
            noProductsPlaceholder = {
                FavoriteProductsNotFoundPlaceholder(
                    onGoToCatalogClicked = onGoToCatalogClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                )
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
