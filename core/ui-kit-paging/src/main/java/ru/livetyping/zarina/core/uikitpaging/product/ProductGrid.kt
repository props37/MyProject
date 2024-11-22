package ru.livetyping.zarina.core.uikitpaging.product

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.paging.retryAppendPrependErrors
import ru.livetyping.zarina.core.uicompose.animateFastScrollToItem
import ru.livetyping.zarina.core.uikit.button.ZarinaScrollToTopButton
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.product.ProductCard
import ru.livetyping.zarina.core.uikit.product.ProductCardSkeleton
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.CellInRowCount
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.FastScrollToTopDistanceThreshold
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.FullscreenItemIndex
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.PlaceholderCount
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.ProductCardArrangement
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.ScrollToTopButtonVisibilityItemThreshold
import timber.log.Timber

// TODO: [Low] Migrate to ZarinaPagingPullRefreshContainer
@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun ProductGrid(
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    onProductClicked: (Product) -> Unit,
    onAddToFavoritesClicked: (Product) -> Unit,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    emptyProductsPlaceholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,

    /**
     * Callback that will be called when products are refreshed. Since the refresh is done
     * under the hood, the additional logic can be invoked using this callback.
     */
    onProductsRefreshed: (() -> Unit)? = null,

    /**
     * Callback that will be called when error retry button is clicked. Since the retry
     * is done under the hood, the additional logic can be invoked using this callback.
     */
    onProductsErrorRefreshClicked: (() -> Unit)? = null,
) {
    val gridState = rememberLazyGridState()
    val productPagingItems = productPagingDataFlow.collectAsLazyPagingItems()

    // TODO: [Medium] Retry on user actions instead of auto retry
    LaunchedEffect(gridState, productPagingItems) {
        productPagingItems.retryAppendPrependErrors(gridState)
    }

    // Scroll to top when Refresh loading completes
    LaunchedEffect(gridState, productPagingItems) {
        var prevLoadState: LoadState? = null
        snapshotFlow { productPagingItems.loadState.refresh }.collect { loadState ->
            if (loadState is LoadState.NotLoading && prevLoadState is LoadState.Loading) {
                gridState.requestScrollToItem(0)
            }
            prevLoadState = loadState
        }
    }

    // Logging
    if (Timber.treeCount > 0) {
        LaunchedEffect(productPagingItems) {
            snapshotFlow { productPagingItems.loadState }
                .collect { loadStates ->
                    val refresh = loadStates.refresh
                    if (refresh is LoadState.Error) Timber.tag(Tag).e(refresh.error)

                    val append = loadStates.append
                    if (append is LoadState.Error) Timber.tag(Tag).e(append.error)

                    val prepend = loadStates.prepend
                    if (prepend is LoadState.Error) Timber.tag(Tag).e(prepend.error)
                }
        }
    }

    Box(modifier = modifier) {
        val isPullRefreshTriggered = remember { mutableStateOf(false) }
        LaunchedEffect(productPagingItems) {
            snapshotFlow { productPagingItems.loadState.refresh }.collect {
                if (it !is LoadState.Loading) {
                    isPullRefreshTriggered.value = false
                }
            }
        }

        val areProductsRefreshing = remember(productPagingItems) {
            derivedStateOf { productPagingItems.loadState.refresh is LoadState.Loading }
        }

        val isPullRefreshing by remember {
            derivedStateOf { isPullRefreshTriggered.value && areProductsRefreshing.value }
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isPullRefreshing,
            onRefresh = {
                isPullRefreshTriggered.value = true
                productPagingItems.refresh()
                onProductsRefreshed?.invoke()
            },
        )

        ZarinaPullRefreshIndicator(
            isRefreshing = isPullRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
        )

        Crossfade(
            targetState = productPagingItems.loadState.refresh,
            label = "ProductGrid",
        ) { loadState ->
            when (loadState) {
                is LoadState.NotLoading -> {
                    Box {
                        ProductGridImpl(
                            productPagingItems = productPagingItems,
                            gridState = gridState,
                            onProductClicked = onProductClicked,
                            onAddToFavoritesClicked = onAddToFavoritesClicked,
                            onAddToCartClicked = onAddToCartClicked,
                            onSubscribeClicked = onSubscribeClicked,
                            emptyProductsPlaceholder = emptyProductsPlaceholder,
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(pullRefreshState),
                        )

                        ScrollToTopButton(
                            gridState = gridState,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 32.dp),
                        )
                    }
                }

                LoadState.Loading -> {
                    ProductGridSkeleton(modifier = Modifier.fillMaxSize())
                }

                is LoadState.Error -> {
                    val state = remember(loadState.error) {
                        ZarinaErrorScreenState.from(loadState.error)
                    }

                    ZarinaErrorScreen(
                        state = state,
                        onButtonClicked = {
                            productPagingItems.retry()
                            onProductsErrorRefreshClicked?.invoke()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductGridImpl(
    productPagingItems: LazyPagingItems<ProductShort>,
    gridState: LazyGridState,
    onProductClicked: (Product) -> Unit,
    onAddToFavoritesClicked: (Product) -> Unit,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    emptyProductsPlaceholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeholderShimmer = rememberZarinaSkeletonShimmer()
    val itemModifier = Modifier.fillMaxWidth()

    Box(modifier = modifier) {
        if (productPagingItems.itemCount > 0) {
            LazyVerticalGrid(
                columns = remember { GridCells.Fixed(CellInRowCount) },
                state = gridState,
                verticalArrangement = ProductCardArrangement,
                horizontalArrangement = ProductCardArrangement,
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                // No need to add append and prepend loaders since item placeholders are used
                // in case of loading
                items(
                    count = productPagingItems.itemCount,
                    span = { index -> getProductGridItemSpan(index) },
                    key = productPagingItems.itemKey { it.id.value },
                    contentType = { index ->
                        getProductGridItemContentType(index, productPagingItems)
                    },
                ) { index ->
                    val product = productPagingItems[index]
                    if (product != null) {
                        ProductCard(
                            product = product,
                            onClick = onProductClicked,
                            onAddToFavoritesClicked = onAddToFavoritesClicked,
                            onAddToCartClicked = onAddToCartClicked,
                            onSubscribeClicked = onSubscribeClicked,
                            shimmer = placeholderShimmer,
                            modifier = itemModifier,
                        )
                    } else {
                        ProductCardSkeleton(
                            shimmer = placeholderShimmer,
                            modifier = itemModifier,
                        )
                    }
                }
            }
        } else {
            emptyProductsPlaceholder()
        }
    }
}

@Composable
private fun ProductGridSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    val itemModifier = Modifier.fillMaxWidth()

    LazyVerticalGrid(
        columns = remember { GridCells.Fixed(CellInRowCount) },
        verticalArrangement = ProductCardArrangement,
        horizontalArrangement = ProductCardArrangement,
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        items(
            count = PlaceholderCount,
            span = { index -> getProductGridItemSpan(index) },
            contentType = { ProductGridContentType.ProductCardPlaceholder },
        ) {
            ProductCardSkeleton(
                shimmer = shimmer,
                modifier = itemModifier,
            )
        }
    }
}

@Composable
private fun ScrollToTopButton(
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val isVisible by remember(gridState) {
        derivedStateOf {
            val isFarEnough =
                gridState.firstVisibleItemIndex >= ScrollToTopButtonVisibilityItemThreshold
            gridState.lastScrolledBackward && isFarEnough
        }
    }

    ZarinaScrollToTopButton(
        isVisible = isVisible,
        onClick = {
            coroutineScope.launch {
                gridState.animateFastScrollToItem(
                    item = 0,
                    distanceThreshold = FastScrollToTopDistanceThreshold,
                )
            }
        },
        modifier = modifier,
    )
}

private fun LazyGridItemSpanScope.getProductGridItemSpan(index: Int): GridItemSpan {
    return if ((index + 1) % FullscreenItemIndex == 0) {
        GridItemSpan(maxCurrentLineSpan)
    } else {
        GridItemSpan(1)
    }
}

private fun getProductGridItemContentType(
    index: Int,
    productPagingItems: LazyPagingItems<ProductShort>,
): ProductGridContentType {
    val product = productPagingItems.peek(index)
    return if (product != null) {
        ProductGridContentType.ProductCard
    } else {
        ProductGridContentType.ProductCardPlaceholder
    }
}

private enum class ProductGridContentType { ProductCard, ProductCardPlaceholder }

internal object ProductGridDefaults {
    const val CellInRowCount = 2
    const val FullscreenItemIndex = 5

    const val PlaceholderCount = 20

    val ProductCardArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(4.dp)

    const val ScrollToTopButtonVisibilityItemThreshold = 20
    const val FastScrollToTopDistanceThreshold = 5
}

private const val Tag = "ProductGrid"
