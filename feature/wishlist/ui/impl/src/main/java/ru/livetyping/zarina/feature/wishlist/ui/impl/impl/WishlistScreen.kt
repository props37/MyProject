package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component.EmptyWishlistPlaceholder
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarState
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistEvent

@Composable
internal fun WishlistScreen(
    navActions: WishlistNavActions,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onWishlistEvent = viewModel::onWishlistEvent,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        onProductEvent = viewModel::onProductEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: TopBarState,
    onWishlistEvent: (WishlistEvent) -> Unit,
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    onProductEvent: (ProductEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<WishlistSideEffect>,
    navActions: WishlistNavActions,
) {
    WishlistScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
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
            onClearWishlistClicked = { onWishlistEvent(WishlistEvent.ClearWishlistClicked) },
        )

        ProductGrid(
            productPagingDataFlow = productPagingDataFlow,
            onProductClicked = { onProductEvent(ProductEvent.ProductClicked(it)) },
            onAddToFavoritesClicked = { onProductEvent(ProductEvent.AddToFavoritesClicked(it)) },
            onAddToCartClicked = { onProductEvent(ProductEvent.AddToCartClicked(it)) },
            onSubscribeClicked = { onProductEvent(ProductEvent.SubscribeClicked(it)) },
            emptyProductsPlaceholder = {
                EmptyWishlistPlaceholder(
                    onGoToCatalogClicked = { onWishlistEvent(WishlistEvent.GoToCatalogClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                )
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
