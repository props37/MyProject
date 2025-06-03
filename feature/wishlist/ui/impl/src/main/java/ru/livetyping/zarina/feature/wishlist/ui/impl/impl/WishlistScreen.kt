package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarHeightAsState
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistState
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui.NoProductsPlaceholder
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui.TopBar
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui.topWindowInsetsScrimGradient

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

    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()

    val backgroundColor = UiKitTheme2.colors.white

    val productPagingItems = wishlistState.productPagingDataFlow.collectAsLazyPagingItems()

    CollapsingTopBarLayout(
        topBar = {
            val alpha by animateFloatAsState(if (productPagingItems.itemCount > 0) 1f else 0f)

            TopBar(
                productCount = wishlistState.productCount,
                alphaProvider = { alpha },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .topWindowInsetsScrimGradient(
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top),
                startColor = backgroundColor.copy(alpha = 0.8f),
                endColor = Color.Transparent,
            ),
    ) { padding ->
        ProductGrid(
            productPagingItems = productPagingItems,
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
