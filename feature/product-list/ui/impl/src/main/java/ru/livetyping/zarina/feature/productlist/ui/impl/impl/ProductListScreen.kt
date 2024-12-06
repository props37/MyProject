package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions

@Composable
internal fun ProductListScreen(
    navActions: ProductListNavActions,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    ScreenContent(
        productPagingDataFlow = viewModel.productPagingDataFlow,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    sideEffects: Flow<ProductListSideEffect>,
    navActions: ProductListNavActions,
) {
    ProductListScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
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
        // TODO: [Top] Add TopBar
        // TODO: [Top] Add collapsable tags
        // TODO: [Top] Implement
        ProductGrid(
            productPagingDataFlow = productPagingDataFlow,
            onProductClicked = {},
            onAddToFavoritesClicked = {},
            onAddToCartClicked = {},
            onSubscribeClicked = {},
            emptyProductsPlaceholder = {},
            onProductsRefreshed = {},
            onProductsErrorRefreshClicked = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
