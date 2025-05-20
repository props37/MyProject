package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarHeightAsState
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.ProductListEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.ProductListState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui.TopBar
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui.UtilityTopBar

@Composable
internal fun ProductListScreen(
    navActions: ProductListNavActions,
    viewModel: ProductListViewModel,
) {
    val productListState by viewModel.productListState.collectAsStateWithLifecycle()

    LifecycleEventEffect(onLifecycleEvent = viewModel::onLifecycleEvent)

    BackHandler(
        enabled = productListState.interceptSystemBack,
        onBack = { viewModel.onProductListEvent(ProductListEvent.SystemBackClicked) },
    )

    ScreenContent(
        productListState = productListState,
        onProductListEvent = viewModel::onProductListEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    productListState: ProductListState,
    onProductListEvent: (ProductListEvent) -> Unit,
    sideEffects: Flow<ProductListSideEffect>,
    navActions: ProductListNavActions,
) {
    ProductListScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()

    CollapsingTopBarLayout(
        topBar = {
            TopBar(
                categoryName = productListState.categoryName,
                subcategoryListState = productListState.subcategoryListState,
                onBackClicked = { onProductListEvent(ProductListEvent.BackClicked) },
                onSubcategoryClicked = {
                    onProductListEvent(ProductListEvent.SubcategoryClicked(it))
                },
                modifier = Modifier.graphicsLayer {
                    alpha = 1f - topBarScrollBehavior.state.collapsedFraction
                },
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        ) {
            UtilityTopBar(
                onBackClicked = { onProductListEvent(ProductListEvent.BackClicked) },
                onFiltersClicked = { onProductListEvent(ProductListEvent.FiltersClicked) },
                onSearchClicked = { onProductListEvent(ProductListEvent.SearchClicked) },
                collapsingProgressProvider = { topBarScrollBehavior.state.collapsedFraction },
            )

            val productGridSideEffect = remember(sideEffects) {
                sideEffects.mapNotNull { it.toProductGridSideEffect() }
            }

            ProductGrid(
                productPagingDataFlow = productListState.productPagingDataFlow,
                onProductClicked = { onProductListEvent(ProductListEvent.ProductClicked(it)) },
                onAddToWishlistClicked = {
                    onProductListEvent(ProductListEvent.AddToWishlistClicked(it))
                },
                onProductsPullRefreshTriggered = {
                    onProductListEvent(ProductListEvent.PullRefreshTriggered)
                },
                onProductsErrorRefreshClicked = {
                    onProductListEvent(ProductListEvent.RefreshClicked)
                },
                noProductsPlaceholder = {
                    // TODO: [Top] Implement
                },
                sideEffects = productGridSideEffect,
                bottomPaddingProvider = { bottomNavBarHeightAsState().value },
                appMetricaScreen = remember { Screen.ProductList(categoryPath = null) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun ProductListSideEffect.toProductGridSideEffect(): ProductGridSideEffect? {
    return when (this) {
        ProductListSideEffect.ScrollProductsToTop -> ProductGridSideEffect.ScrollToTop
        else -> null
    }
}
