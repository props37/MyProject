package ru.livetyping.zarina.ui.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.ui.common.component.button.ZarinaScrollToTopButton
import ru.livetyping.zarina.ui.common.component.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.ui.common.error.ErrorState
import ru.livetyping.zarina.ui.common.error.from
import ru.livetyping.zarina.ui.common.util.library.paging.retryAppendPrependErrors
import ru.livetyping.zarina.util.compose.animateFastScrollToItem
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.collectIsScrollingBackwardAsState
import ru.livetyping.zarina.util.library.paging3.PagingErrorTimberLogger

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductGrid(
    productPagingDataFlow: Flow<PagingData<Product>>,
    onProductClicked: (Product) -> Unit,
    onAddToFavoritesClicked: (Product) -> Unit,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    noProductsPlaceholder: @Composable () -> Unit,
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

    LaunchedEffect(gridState, productPagingItems) {
        productPagingItems.retryAppendPrependErrors(gridState)
    }

    // Scroll to top when Refresh loading completes
    LaunchedEffect(gridState, productPagingItems) {
        var prevLoadState: LoadState? = null
        snapshotFlow { productPagingItems.loadState.refresh }.collect { loadState ->
            if (loadState is LoadState.NotLoading && prevLoadState is LoadState.Loading) {
                gridState.scrollToItem(0)
            }
            prevLoadState = loadState
        }
    }

    PagingErrorTimberLogger(pagingItems = productPagingItems)

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
            refreshing = isPullRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
        )

        Crossfade(
            targetState = productPagingItems.loadState.refresh,
            label = "Products content",
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
                            noProductsPlaceholder = noProductsPlaceholder,
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
                    val placeholderShimmer = rememberZarinaSkeletonShimmer()
                    val itemModifier = Modifier.fillMaxWidth()

                    LazyVerticalGrid(
                        columns = remember { GridCells.Fixed(ProductGridCellInRowCount) },
                        state = gridState,
                        verticalArrangement = ProductGridArrangement,
                        horizontalArrangement = ProductGridArrangement,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            count = ProductGridPlaceholderCount,
                            span = { index -> getProductGridItemSpan(index) },
                            contentType = { ProductGridContentTypeProductCardPlaceholder },
                        ) {
                            ProductCardSkeleton(
                                shimmer = placeholderShimmer,
                                modifier = itemModifier,
                            )
                        }
                    }
                }

                is LoadState.Error -> {
                    val state = remember(loadState.error) {
                        ErrorState.from(loadState.error)
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
    productPagingItems: LazyPagingItems<Product>,
    gridState: LazyGridState,
    onProductClicked: (Product) -> Unit,
    onAddToFavoritesClicked: (Product) -> Unit,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    noProductsPlaceholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeholderShimmer = rememberZarinaSkeletonShimmer()
    val itemModifier = Modifier.fillMaxWidth()

    Box(modifier = modifier) {
        if (productPagingItems.itemCount > 0) {
            LazyVerticalGrid(
                columns = remember { GridCells.Fixed(ProductGridCellInRowCount) },
                state = gridState,
                verticalArrangement = ProductGridArrangement,
                horizontalArrangement = ProductGridArrangement,
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
            noProductsPlaceholder()
        }
    }
}

@Composable
private fun ScrollToTopButton(
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val isScrollingBackwardState = gridState.collectIsScrollingBackwardAsState()
    val isVisible by remember(gridState, isScrollingBackwardState) {
        derivedStateOf {
            val isScrollingBackward = isScrollingBackwardState.value
            val isFarEnough =
                gridState.firstVisibleItemIndex >= ScrollToTopButtonVisibilityItemThreshold
            isScrollingBackward && isFarEnough
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
    return if ((index + 1) % ProductGridFullscreenItemIndex == 0) {
        GridItemSpan(maxCurrentLineSpan)
    } else {
        GridItemSpan(1)
    }
}

private fun getProductGridItemContentType(
    index: Int,
    productPagingItems: LazyPagingItems<Product>,
): String {
    val product = productPagingItems.peek(index)
    return if (product != null) {
        ProductGridContentTypeProductCard
    } else {
        ProductGridContentTypeProductCardPlaceholder
    }
}

private const val ProductGridCellInRowCount = 2
private const val ProductGridFullscreenItemIndex = 5

private const val ProductGridPlaceholderCount = 20

private val ProductGridArrangement: Arrangement.HorizontalOrVertical
    get() = Arrangement.spacedBy(4.dp)

private const val ProductGridContentTypeProductCard = "ProductGridContentTypeProduct"
private const val ProductGridContentTypeProductCardPlaceholder =
    "ProductGridContentTypeProductCardPlaceholder"

private const val ScrollToTopButtonVisibilityItemThreshold = 20
private const val FastScrollToTopDistanceThreshold = 5
