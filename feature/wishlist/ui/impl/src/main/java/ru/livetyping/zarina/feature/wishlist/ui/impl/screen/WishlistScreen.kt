package ru.livetyping.zarina.feature.wishlist.ui.impl.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uicompose.list.canScroll
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlur
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlurDefaults
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarHeightAsState
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.impl.screen.model.WishlistEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.screen.model.WishlistState
import ru.livetyping.zarina.feature.wishlist.ui.impl.screen.ui.NoProductsPlaceholder
import ru.livetyping.zarina.feature.wishlist.ui.impl.screen.ui.TopBar

@Composable
internal fun WishlistScreen(
    navActions: WishlistFeature.NavActions,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    BackHandler { viewModel.onWishlistEvent(WishlistEvent.BackClicked) }

    LifecycleEventEffect(onLifecycleEvent = viewModel::onLifecycleEvent)

    val wishlistState by viewModel.wishlistState.collectAsStateWithLifecycle()

    ScreenContent(
        wishlistState = wishlistState,
        onWishlistEvent = viewModel::onWishlistEvent,
        productGridSideEffects = viewModel.productGridSideEffects,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    wishlistState: WishlistState,
    onWishlistEvent: (WishlistEvent) -> Unit,
    productGridSideEffects: Flow<ProductGridSideEffect>,
    sideEffects: Flow<WishlistSideEffect>,
    navActions: WishlistFeature.NavActions,
) {
    WishlistScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    val backgroundColor = UiKitTheme2.colors.white

    val productPagingItems = wishlistState.productPagingDataFlow.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyGridState()

    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior(
        canScroll = { lazyGridState.canScroll },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        val hazeState = rememberHazeState(StatusBarBlurDefaults.isStatusBarBlurEnabled())

        StatusBarBlur(
            hazeState = hazeState,
            modifier = Modifier.zIndex(1f),
        )

        CollapsingTopBarLayout(
            topBar = {
                val alpha by animateFloatAsState(if (productPagingItems.itemCount > 0) 1f else 0f)

                TopBar(
                    productCount = wishlistState.productCount,
                    alphaProvider = { alpha },
                )
            },
            scrollBehavior = topBarScrollBehavior,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState),
        ) { padding ->
            ProductGrid(
                productPagingItems = productPagingItems,
                gridState = lazyGridState,
                noProductsPlaceholder = {
                    NoProductsPlaceholder(
                        onCategoryShortcutClicked = {
                            onWishlistEvent(WishlistEvent.CategoryShortcutClicked(it))
                        },
                        onSearchClicked = { onWishlistEvent(WishlistEvent.SearchClicked) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = bottomNavBarHeightAsState().value)
                            .verticalScroll(rememberScrollState()),
                    )
                },
                onProductClicked = { onWishlistEvent(WishlistEvent.ProductClicked(it)) },
                onAddToWishlistClicked = { onWishlistEvent(WishlistEvent.AddProductToWishlistClicked(it)) },
                onProductsPullRefreshTriggered = { onWishlistEvent(WishlistEvent.RefreshTriggered) },
                onProductsAppendError = { onWishlistEvent(WishlistEvent.ProductAppendError(it)) },
                onProductsPrependError = { onWishlistEvent(WishlistEvent.ProductAppendError(it)) },
                onProductsErrorRefreshClicked = { onWishlistEvent(WishlistEvent.RefreshTriggered) },
                sideEffects = productGridSideEffects,
                bottomPaddingProvider = { bottomNavBarHeightAsState().value },
                appMetricaScreen = Screen.Wishlist,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            )
        }
    }
}
