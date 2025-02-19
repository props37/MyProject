package ru.livetyping.zarina.presentation.screen.favorites

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import ru.livetyping.zarina.data.analytics.AppMetricaScreen
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.ProductGrid
import ru.livetyping.zarina.presentation.common.component.ProductGridSideEffect
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesScreenComponents.FavoriteProductsNotFoundPlaceholder
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun FavoritesScreen(
    navigate: (FavoritesScreenAction) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val isClearFavoritesButtonVisible by viewModel.isClearFavoritesButtonVisible.collectAsStateWithLifecycle()

    ScreenContent(
        productPagingDataFlow = viewModel.productPagingDataFlow,
        productGridSideEffects = viewModel.productGridSideEffects,
        onProductClicked = viewModel::onProductClicked,
        onAddProductToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
        onAddProductToCartClicked = viewModel::onAddProductToCartClicked,
        onSubscribeToProductClicked = viewModel::onSubscribeToProductClicked,
        isClearFavoritesButtonVisible = isClearFavoritesButtonVisible,
        onClearFavoritesClicked = viewModel::onClearFavoritesClicked,
        onGoToCatalogClicked = viewModel::onGoToCatalogClicked,
        onScreenCreated = viewModel::onScreenCreated,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    productPagingDataFlow: Flow<PagingData<ProductItem>>,
    productGridSideEffects: Flow<ProductGridSideEffect>,
    onProductClicked: (Product) -> Unit,
    onAddProductToFavoritesClicked: (Product) -> Unit,
    onAddProductToCartClicked: (Product) -> Unit,
    onSubscribeToProductClicked: (Product) -> Unit,
    isClearFavoritesButtonVisible: Boolean,
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
            isClearButtonVisible = isClearFavoritesButtonVisible,
            onClearClicked = onClearFavoritesClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        ProductGrid(
            productPagingDataFlow = productPagingDataFlow,
            onProductClicked = onProductClicked,
            onAddToFavoritesClicked = onAddProductToFavoritesClicked,
            onAddToCartClicked = onAddProductToCartClicked,
            onSubscribeClicked = onSubscribeToProductClicked,
            noProductsPlaceholder = {
                FavoriteProductsNotFoundPlaceholder(
                    onGoToCatalogClicked = onGoToCatalogClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                )
            },
            appMetricaScreen = AppMetricaScreen.Wishlist,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            productPagingDataFlow = remember {
                flowOf(PagingData.from(FakeDataGenerator.getProductItems()))
            },
            productGridSideEffects = emptyFlow(),
            onProductClicked = {},
            onAddProductToFavoritesClicked = {},
            onAddProductToCartClicked = {},
            onSubscribeToProductClicked = {},
            isClearFavoritesButtonVisible = true,
            onClearFavoritesClicked = {},
            onGoToCatalogClicked = {},
            onScreenCreated = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
