package ru.zarina.zarina.ui.screen.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.component.ProductCard
import ru.zarina.zarina.ui.common.component.ProductCardPlaceholder
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.button.ZarinaIconButton
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.util.library.paging.retryAppendPrependErrors
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.Crossfade
import ru.zarina.zarina.util.compose.animateFastScrollToItem
import ru.zarina.zarina.util.compose.collectIsScrollingBackwardAsState
import ru.zarina.zarina.util.compose.unscalable
import timber.log.Timber
import java.io.IOException

object ProductsScreenComponents {

    @Composable
    fun TopBar(
        title: String?,
        appliedFilterCount: Int,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = TopBarDefaults.MinHeight)
                .padding(vertical = TopBarDefaults.VerticalPadding),
        ) {
            BackIconButton(
                onClick = actions.onBackClicked,
                iconSize = TopBarIconSize,
                modifier = Modifier.padding(start = 2.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))

            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                contentAlignment = Alignment.CenterStart,
                label = "TopBar title",
                modifier = Modifier.weight(1f),
            ) { title ->
                if (title != null) {
                    Text(
                        text = title,
                        style = UiKitTheme.typographyReworked.primary.regular,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Skeleton(
                        modifier = Modifier
                            .wrapContentWidth(align = Alignment.Start)
                            .fillMaxWidth(fraction = 0.5f)
                            .height(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            ZarinaIconButton(
                onClick = actions.onSearchClicked,
                indication = rememberRipple(bounded = false, radius = TopBarIconSize),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_24),
                    contentDescription = stringResource(R.string.search),
                    tint = UiKitTheme.colorsReworked.icon.regular.default,
                    modifier = Modifier.size(TopBarIconSize),
                )
            }

            Box {
                ZarinaIconButton(
                    onClick = actions.onFiltersClicked,
                    indication = rememberRipple(bounded = false, radius = TopBarIconSize),
                    modifier = Modifier.padding(end = 2.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filters_24),
                        contentDescription = stringResource(R.string.filters),
                        tint = UiKitTheme.colorsReworked.icon.regular.default,
                        modifier = Modifier.size(TopBarIconSize),
                    )
                }

                AppliedFilterCounter(
                    appliedFilterCount = appliedFilterCount,
                    modifier = Modifier.align(AppliedFilterCounterAlignment),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Products(
        productPagingDataFlow: Flow<PagingData<Product>>,
        onRefreshProducts: () -> Unit,
        onProductsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
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

        Box(modifier = modifier) {
            var canPullRefreshIndicatorBeShown by remember(productPagingItems) {
                mutableStateOf(false)
            }
            LaunchedEffect(productPagingItems) {
                snapshotFlow { productPagingItems.loadState.refresh }
                    .firstOrNull { it is LoadState.NotLoading }
                    .also { canPullRefreshIndicatorBeShown = true }
            }

            val isRefreshing = canPullRefreshIndicatorBeShown
                    && productPagingItems.loadState.refresh is LoadState.Loading

            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = {
                    productPagingItems.refresh()
                    onRefreshProducts()
                },
            )

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                backgroundColor = UiKitTheme.colorsReworked.background.general.regular.default,
                contentColor = UiKitTheme.colorsReworked.icon.regular.default,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            )

            Crossfade(
                targetState = productPagingItems.loadState.refresh,
                contentKey = { it !is LoadState.Error },
                label = "Products content",
                modifier = Modifier.matchParentSize(),
            ) { loadState ->
                if (loadState !is LoadState.Error) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ProductGrid(
                            productPagingItems = productPagingItems,
                            gridState = gridState,
                            loadState = loadState,
                            modifier = Modifier
                                .matchParentSize()
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
                } else {
                    val state = remember(loadState.error) {
                        when (loadState.error) {
                            is IOException -> ErrorStateRework.NETWORK
                            else -> ErrorStateRework.GENERIC
                        }
                    }

                    ZarinaErrorScreen(
                        state = state,
                        onRefreshClicked = {
                            productPagingItems.retry()
                            onProductsErrorRefreshClicked()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ProductGrid(
        productPagingItems: LazyPagingItems<Product>,
        gridState: LazyGridState,
        loadState: LoadState,
        modifier: Modifier = Modifier,
    ) {
        val arrangement = remember { Arrangement.spacedBy(4.dp) }
        val placeholderShimmer = rememberSkeletonShimmer()
        val itemModifier = Modifier.fillMaxWidth()

        LazyVerticalGrid(
            columns = remember { GridCells.Fixed(ProductGridCellInRowCount) },
            state = gridState,
            verticalArrangement = arrangement,
            horizontalArrangement = arrangement,
            modifier = modifier,
        ) {
            if (loadState is LoadState.NotLoading) {
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
                            onClick = { /*TODO*/ },
                            onAddToFavoritesClicked = { /*TODO*/ },
                            onAddToCartClicked = { /*TODO*/ },
                            shimmer = placeholderShimmer,
                            modifier = itemModifier,
                        )
                    } else {
                        ProductCardPlaceholder(
                            shimmer = placeholderShimmer,
                            modifier = itemModifier,
                        )
                    }
                }
            } else {
                items(
                    count = ProductGridPlaceholderCount,
                    span = { index -> getProductGridItemSpan(index) },
                    contentType = { ProductGridContentTypeProductCardPlaceholder },
                ) {
                    ProductCardPlaceholder(
                        shimmer = placeholderShimmer,
                        modifier = itemModifier,
                    )
                }
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
                val isFarEnough = gridState.firstVisibleItemIndex >= ScrollToTopButtonVisibilityItemThreshold
                isScrollingBackward && isFarEnough
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = AnimatedContentDefaultEnterTransition,
            exit = AnimatedContentDefaultExitTransition,
            modifier = modifier,
        ) {
            ZarinaButton(
                onClick = {
                    coroutineScope.launch {
                        gridState.animateFastScrollToItem(
                            item = 0,
                            distanceThreshold = FastScrollToTopDistanceThreshold,
                        )
                    }
                },
                size = ZarinaButtonSize.Medium,
                shape = CircleShape,
                contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left_24),
                    contentDescription = stringResource(R.string.scroll_to_top),
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            }
        }
    }

    @Composable
    private fun AppliedFilterCounter(
        appliedFilterCount: Int,
        modifier: Modifier = Modifier,
    ) {
        if (appliedFilterCount > 0) {
            Text(
                text = appliedFilterCount.toString(),
                style = UiKitTheme.typographyReworked.caption2.bold.unscalable(LocalDensity.current),
                color = UiKitTheme.colorsReworked.text.general.inversed.default,
                modifier = modifier
                    .background(
                        color = UiKitTheme.colorsReworked.background.general.inversed.default,
                        shape = CircleShape,
                    )
                    .padding(start = 6.dp, top = 1.dp, end = 6.dp),
            )
        }
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

    @Stable
    class TopBarActions(
        val onBackClicked: () -> Unit,
        val onSearchClicked: () -> Unit,
        val onFiltersClicked: () -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopBarActions

            if (onBackClicked != other.onBackClicked) return false
            if (onSearchClicked != other.onSearchClicked) return false
            return onFiltersClicked == other.onFiltersClicked
        }

        override fun hashCode(): Int {
            var result = onBackClicked.hashCode()
            result = 31 * result + onSearchClicked.hashCode()
            result = 31 * result + onFiltersClicked.hashCode()
            return result
        }
    }

    private val TopBarIconSize: Dp get() = 20.dp

    private const val ProductGridCellInRowCount = 2
    private const val ProductGridFullscreenItemIndex = 5

    private const val ProductGridPlaceholderCount = 20

    private const val ProductGridContentTypeProductCard = "ProductGridContentTypeProduct"
    private const val ProductGridContentTypeProductCardPlaceholder =
        "ProductGridContentTypeProductCardPlaceholder"

    private const val ScrollToTopButtonVisibilityItemThreshold = 20
    private const val FastScrollToTopDistanceThreshold = 5

    private val AppliedFilterCounterAlignment: Alignment
        get() = BiasAlignment(0.5f, -0.5f)
}
