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
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicomponent.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorModalBottomSheet
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component.EmptyWishlistPlaceholder
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarState
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistEvent

@Composable
internal fun WishlistScreen(
    navActions: WishlistNavActions,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val visibleProductSizeSelector by viewModel.visibleProductSizeSelector.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        onWishlistEvent = viewModel::onWishlistEvent,
        visibleProductSizeSelector = visibleProductSizeSelector,
        onSizeSelectorEvent = viewModel::onSizeSelectorEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    onWishlistEvent: (WishlistEvent) -> Unit,
    visibleProductSizeSelector: Product?,
    onSizeSelectorEvent: (SizeSelectorEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<WishlistSideEffect>,
    navActions: WishlistNavActions,
) {
    WishlistScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
        onBackClicked = onBackClicked,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    if (visibleProductSizeSelector != null) {
        SizeSelectorModalBottomSheet(
            product = visibleProductSizeSelector,
            onEvent = onSizeSelectorEvent,
        )
    }

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

        ProductGrid(
            productPagingDataFlow = productPagingDataFlow,
            onProductClicked = { onWishlistEvent(WishlistEvent.ProductClicked(it)) },
            onAddToWishlistClicked = { onWishlistEvent(WishlistEvent.AddToWishlistClicked(it)) },
            onAddToCartClicked = { onWishlistEvent(WishlistEvent.AddToCartClicked(it)) },
            onSubscribeClicked = { onWishlistEvent(WishlistEvent.SubscribeClicked(it)) },
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
