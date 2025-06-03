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
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.ProductListEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.ProductListState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui.NoProductsPlaceholder
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui.TopBar
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui.UtilityTopBar
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui.loadMoreProductsButtonGridItem

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
        productGridSideEffects = viewModel.productGridSideEffects,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    productListState: ProductListState,
    onProductListEvent: (ProductListEvent) -> Unit,
    productGridSideEffects: Flow<ProductGridSideEffect>,
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
                onSeeAllProductsInCategoryClicked = {
                    onProductListEvent(ProductListEvent.SeeAllProductsInCategoryClicked)
                },
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
                appliedFilterCount = productListState.appliedFilterCount,
                onBackClicked = { onProductListEvent(ProductListEvent.BackClicked) },
                onFiltersClicked = { onProductListEvent(ProductListEvent.FiltersClicked) },
                onSearchClicked = { onProductListEvent(ProductListEvent.SearchClicked) },
                collapsingProgressProvider = { topBarScrollBehavior.state.collapsedFraction },
            )

            val footer: (LazyGridScope.() -> Unit)? =
                if (productListState.isLoadMoreProductsButtonVisible) {
                    {
                        loadMoreProductsButtonGridItem(
                            onClick = {
                                onProductListEvent(ProductListEvent.LoadMoreProductsClicked)
                            },
                        )
                    }
                } else null

            ProductGrid(
                productPagingItems = productListState.productPagingDataFlow.collectAsLazyPagingItems(),
                onProductClicked = { onProductListEvent(ProductListEvent.ProductClicked(it)) },
                onAddToWishlistClicked = {
                    onProductListEvent(ProductListEvent.AddToWishlistClicked(it))
                },
                onProductsPullRefreshTriggered = {
                    onProductListEvent(ProductListEvent.PullRefreshTriggered)
                },
                onProductsAppendError = {
                    onProductListEvent(ProductListEvent.ProductAppendError(it))
                },
                onProductsPrependError = {
                    onProductListEvent(ProductListEvent.ProductPrependError(it))
                },
                onProductsErrorRefreshClicked = {
                    onProductListEvent(ProductListEvent.RefreshClicked)
                },
                noProductsPlaceholder = {
                    NoProductsPlaceholder(
                        onCategoryShortcutClicked = { categoryId ->
                            onProductListEvent(ProductListEvent.CategoryShortcutClicked(categoryId))
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = bottomNavBarHeightAsState().value),
                    )
                },
                footer = footer,
                sideEffects = productGridSideEffects,
                isEndlessLoadingEnabled = productListState.isProductEndlessLoadingEnabled,
                bottomPaddingProvider = { bottomNavBarHeightAsState().value },
                appMetricaScreen = remember { Screen.ProductList(categoryPath = null) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}
