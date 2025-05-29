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
import androidx.compose.foundation.lazy.grid.LazyGridScope
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
import androidx.compose.ui.unit.Dp
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
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uicompose.list.animateFastScrollToItem
import ru.livetyping.zarina.core.uikit.button.ZarinaScrollToTopButton
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen2
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.product.ProductCard
import ru.livetyping.zarina.core.uikit.product.ProductCardSkeleton
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.FastScrollToTopDistanceThreshold
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.ItemInRowCount
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.LoadingSkeletonCount
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.PackFullSizeItemIndices
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.PackSize
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.ProductCardHorizontalArrangement
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.ProductCardVerticalArrangement
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridDefaults.ScrollToTopButtonPadding
import timber.log.Timber

// TODO: [Top] Show append and prepend errors to user
// TODO: [Low] Migrate to ZarinaPagingPullRefreshContainer

@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun ProductGrid(
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    onProductClicked: (Product) -> Unit,
    onAddToWishlistClicked: (Product) -> Unit,
    noProductsPlaceholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    footer: (LazyGridScope.() -> Unit)? = null,

    /**
     * Callback that will be called when products are refreshed. Since the refresh is done
     * under the hood, the additional logic can be invoked using this callback.
     */
    onProductsPullRefreshTriggered: (() -> Unit)? = null,

    /**
     * Callback that will be called when error retry button is clicked. Since the retry
     * is done under the hood, the additional logic can be invoked using this callback.
     */
    onProductsErrorRefreshClicked: (() -> Unit)? = null,
    sideEffects: Flow<ProductGridSideEffect>? = null,
    isEndlessLoadingEnabled: Boolean = true,
    bottomPaddingProvider: @Composable () -> Dp = { 0.dp },
    appMetricaScreen: Screen? = null,
) {
    val gridState = rememberLazyGridState()
    val productPagingItems = productPagingDataFlow.collectAsLazyPagingItems()

    if (Timber.treeCount > 0) {
        Logging(productPagingItems)
    }

    SideEffectObserver(gridState, sideEffects)

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
                onProductsPullRefreshTriggered?.invoke()
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
                            onAddToWishlistClicked = onAddToWishlistClicked,
                            noProductsPlaceholder = noProductsPlaceholder,
                            footer = footer,
                            isEndlessLoadingEnabled = isEndlessLoadingEnabled,
                            bottomPaddingProvider = bottomPaddingProvider,
                            appMetricaScreen = appMetricaScreen,
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(pullRefreshState),
                        )

                        val scrollToTopButtonBottomPadding =
                            bottomPaddingProvider() + ScrollToTopButtonPadding

                        ScrollToTopButton(
                            gridState = gridState,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.BottomEnd)
                                .padding(
                                    end = ScrollToTopButtonPadding,
                                    bottom = scrollToTopButtonBottomPadding,
                                ),
                        )
                    }
                }

                LoadState.Loading -> {
                    val shimmer = rememberZarinaSkeletonShimmer()

                    ProductGridSkeleton(
                        shimmer = shimmer,
                        bottomPaddingProvider = bottomPaddingProvider,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is LoadState.Error -> {
                    val errorState = remember(loadState.error) {
                        ZarinaErrorScreenState2.from(loadState.error)
                    }

                    ZarinaErrorScreen2(
                        state = errorState,
                        onButtonClick = {
                            productPagingItems.retry()
                            onProductsErrorRefreshClicked?.invoke()
                        },
                        bottomPaddingProvider = bottomPaddingProvider,
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
    onAddToWishlistClicked: (Product) -> Unit,
    noProductsPlaceholder: @Composable () -> Unit,
    footer: (LazyGridScope.() -> Unit)?,
    isEndlessLoadingEnabled: Boolean,
    bottomPaddingProvider: @Composable () -> Dp,
    appMetricaScreen: Screen?,
    modifier: Modifier = Modifier,
) {
    val placeholderShimmer = rememberZarinaSkeletonShimmer()
    val itemModifier = Modifier.fillMaxWidth()

    Box(modifier = modifier) {
        if (productPagingItems.itemCount > 0) {
            val bottomPadding =
                bottomPaddingProvider() + ZarinaScrollableDefaults.ScrollableBottomPadding

            LazyVerticalGrid(
                columns = remember { GridCells.Fixed(ItemInRowCount) },
                state = gridState,
                verticalArrangement = ProductCardVerticalArrangement,
                horizontalArrangement = ProductCardHorizontalArrangement,
                contentPadding = PaddingValues(bottom = bottomPadding),
                modifier = Modifier.fillMaxSize(),
            ) {
                prependAppendItems(productPagingItems.loadState.prepend, placeholderShimmer)

                items(
                    count = productPagingItems.itemCount,
                    span = { index -> getProductGridItemSpan(index) },
                    key = productPagingItems.itemKey { it.id.value },
                    contentType = { index ->
                        getProductGridItemContentType(index, productPagingItems)
                    },
                ) { index ->
                    val product = if (isEndlessLoadingEnabled) {
                        productPagingItems[index]
                    } else {
                        productPagingItems.peek(index)
                    }

                    if (product != null) {
                        ProductCard(
                            product = product,
                            onClick = onProductClicked,
                            onAddToWishlistClicked = onAddToWishlistClicked,
                            mediaShimmer = placeholderShimmer,
                            appMetricaScreen = appMetricaScreen,
                            modifier = itemModifier.animateZarinaItem(this),
                        )
                    } else {
                        ProductCardSkeleton(
                            shimmer = placeholderShimmer,
                            modifier = itemModifier.animateZarinaItem(this),
                        )
                    }
                }

                prependAppendItems(productPagingItems.loadState.append, placeholderShimmer)

                footer?.invoke(this)
            }
        } else {
            noProductsPlaceholder()
        }
    }
}

@Composable
private fun ProductGridSkeleton(
    shimmer: Shimmer,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    val bottomPadding = bottomPaddingProvider() + ZarinaScrollableDefaults.ScrollableBottomPadding

    val itemModifier = Modifier.fillMaxWidth()

    LazyVerticalGrid(
        columns = remember { GridCells.Fixed(ItemInRowCount) },
        verticalArrangement = ProductCardVerticalArrangement,
        horizontalArrangement = ProductCardHorizontalArrangement,
        contentPadding = PaddingValues(bottom = bottomPadding),
        modifier = modifier,
    ) {
        items(
            count = PackSize,
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

private fun LazyGridScope.prependAppendItems(
    loadState: LoadState,
    shimmer: Shimmer,
) {
    when (loadState) {
        LoadState.Loading -> {
            items(
                count = LoadingSkeletonCount,
                span = { index -> getProductGridItemSpan(index) },
                contentType = { ProductGridContentType.ProductCardPlaceholder },
            ) {
                ProductCardSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.animateZarinaItem(this),
                )
            }
        }

        is LoadState.Error -> {
            // TODO: [Top] Implement
        }

        is LoadState.NotLoading -> Unit
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
                gridState.firstVisibleItemIndex >= PackSize
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

@Composable
private fun SideEffectObserver(
    lazyGridState: LazyGridState,
    sideEffects: Flow<ProductGridSideEffect>?,
) {
    LaunchedEffect(lazyGridState, sideEffects) {
        sideEffects?.collect {
            when (it) {
                is ProductGridSideEffect.ScrollToTop -> {
                    if (it.animate) {
                        lazyGridState.animateFastScrollToItem(0, FastScrollToTopDistanceThreshold)
                    } else {
                        lazyGridState.requestScrollToItem(0)
                    }
                }
            }
        }
    }
}

@Composable
private fun Logging(productPagingItems: LazyPagingItems<ProductShort>) {
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

private fun LazyGridItemSpanScope.getProductGridItemSpan(index: Int): GridItemSpan {
    val indexInPack = index % PackSize
    return if (indexInPack in PackFullSizeItemIndices) {
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
    const val ItemInRowCount = 2

    val ProductCardHorizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(1.dp)
    val ProductCardVerticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(4.dp)

    const val FastScrollToTopDistanceThreshold = 5

    // 8 small + 2 big + 8 small
    const val PackSize = 8 + 2 + 8
    val PackFullSizeItemIndices = 8..9

    const val LoadingSkeletonCount = 8

    val ScrollToTopButtonPadding: Dp get() = 10.dp
}

private const val Tag = "ProductGrid"
