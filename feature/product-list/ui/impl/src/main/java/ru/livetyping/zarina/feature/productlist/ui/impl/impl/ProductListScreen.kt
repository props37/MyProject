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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarState

@Composable
internal fun ProductListScreen(
    navActions: ProductListNavActions,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        onProductEvent = viewModel::onProductEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    onProductEvent: (ProductEvent) -> Unit,
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
        TopBar(
            state = topBarState,
            onEvent = onTopBarEvent,
        )

        // TODO: [Top] Add collapsable tags
        ProductGrid(
            productPagingDataFlow = productPagingDataFlow,
            onProductClicked = { onProductEvent(ProductEvent.ProductClicked(it)) },
            onAddToWishlistClicked = { onProductEvent(ProductEvent.AddToWishlistClicked(it)) },
            onAddToCartClicked = { onProductEvent(ProductEvent.AddToCartClicked(it)) },
            onSubscribeClicked = { onProductEvent(ProductEvent.SubscribeClicked(it)) },
            onProductsRefreshed = { onProductEvent(ProductEvent.ProductsRefreshed) },
            onProductsErrorRefreshClicked = {
                onProductEvent(ProductEvent.ProductsErrorRefreshClicked)
            },
            emptyProductsPlaceholder = {
                // TODO: [Top] Implement
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
