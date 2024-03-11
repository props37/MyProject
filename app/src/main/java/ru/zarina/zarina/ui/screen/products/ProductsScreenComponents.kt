package ru.zarina.zarina.ui.screen.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.component.ProductCard
import ru.zarina.zarina.ui.common.component.ProductCardPlaceholder
import ru.zarina.zarina.ui.common.component.ZarinaTag
import ru.zarina.zarina.ui.common.component.ZarinaTagSkeleton
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
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.TagListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.Crossfade
import ru.zarina.zarina.util.compose.animateFastScrollToItem
import ru.zarina.zarina.util.compose.collectIsScrollingBackwardAsState
import ru.zarina.zarina.util.compose.unscalable
import ru.zarina.zarina.util.library.paging3.PagingErrorTimberLogger
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
                        style = UiKitTheme.typography.primary.regular,
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

    @Composable
    fun Tags(
        state: TagListState?,
        selectedTagId: Category.Id?,
        onTagClicked: (Category) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                if (initialState != null && targetState != null) {
                    fadeIn() togetherWith fadeOut()
                } else {
                    AnimatedContentDefaultTransitionSpec()
                }.using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.Center,
            contentKey = {
                when (it) {
                    is TagListState.TagList -> TagListContentKeyTagList
                    TagListState.Loading -> it
                    null -> it
                }
            },
            label = "Tags",
            modifier = modifier,
        ) { state ->
            val horizontalArrangement = Arrangement.spacedBy(8.dp)
            val contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)

            when (state) {
                is TagListState.TagList -> {
                    LazyRow(
                        horizontalArrangement = horizontalArrangement,
                        contentPadding = contentPadding,
                    ) {
                        items(
                            items = state.tags,
                            key = { it.id.value },
                        ) { tag ->
                            ZarinaTag(
                                onClick = { onTagClicked(tag) },
                                isSelected = tag.id == selectedTagId,
                            ) {
                                Text(text = tag.name)
                            }
                        }
                    }
                }

                TagListState.Loading -> {
                    val skeletonShimmer = rememberSkeletonShimmer()
                    LazyRow(
                        horizontalArrangement = horizontalArrangement,
                        contentPadding = contentPadding,
                    ) {
                        items(count = 10) {
                            ZarinaTagSkeleton(shimmer = skeletonShimmer)
                        }
                    }
                }

                null -> Unit
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Products(
        productPagingDataFlow: Flow<PagingData<Product>>,
        productCardActions: ProductCardActions,
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
                    onRefreshProducts()
                },
            )

            PullRefreshIndicator(
                refreshing = isPullRefreshing,
                state = pullRefreshState,
                backgroundColor = UiKitTheme.colorsReworked.background.general.regular.default,
                contentColor = UiKitTheme.colorsReworked.icon.regular.default,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            )

            Crossfade(
                targetState = productPagingItems.loadState.refresh,
                label = "Products content",
                modifier = Modifier.matchParentSize(),
            ) { loadState ->
                when (loadState) {
                    is LoadState.NotLoading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ProductGrid(
                                productPagingItems = productPagingItems,
                                gridState = gridState,
                                productCardActions = productCardActions,
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
                    }

                    LoadState.Loading -> {
                        val placeholderShimmer = rememberSkeletonShimmer()
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
                                ProductCardPlaceholder(
                                    shimmer = placeholderShimmer,
                                    modifier = itemModifier,
                                )
                            }
                        }
                    }

                    is LoadState.Error -> {
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
    }

    @Composable
    private fun ProductGrid(
        productPagingItems: LazyPagingItems<Product>,
        gridState: LazyGridState,
        productCardActions: ProductCardActions,
        modifier: Modifier = Modifier,
    ) {
        val placeholderShimmer = rememberSkeletonShimmer()
        val itemModifier = Modifier.fillMaxWidth()

        if (productPagingItems.itemCount > 0) {
            LazyVerticalGrid(
                columns = remember { GridCells.Fixed(ProductGridCellInRowCount) },
                state = gridState,
                verticalArrangement = ProductGridArrangement,
                horizontalArrangement = ProductGridArrangement,
                modifier = modifier,
            ) {
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
                            onClick = { productCardActions.onProductClicked(product) },
                            onAddToFavoritesClicked = {
                                productCardActions.onAddToFavoritesClicked(product)
                            },
                            onAddToCartClicked = { productCardActions.onAddToCartClicked(product) },
                            onSubscribeClicked = { productCardActions.onSubscribeClicked(product) },
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
            }
        } else {
            ProductsNotFound(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
            )
        }
    }

    @Composable
    private fun ProductsNotFound(
        modifier: Modifier = Modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search_24),
                contentDescription = null,
                tint = UiKitTheme.colorsReworked.icon.regular.disabled,
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.could_not_find_products),
                style = UiKitTheme.typography.primary.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.try_select_another_category),
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
            )
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
                style = UiKitTheme.typography.caption2.bold.unscalable(LocalDensity.current),
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

    @Stable
    class ProductCardActions(
        val onProductClicked: (Product) -> Unit,
        val onAddToFavoritesClicked: (Product) -> Unit,
        val onAddToCartClicked: (Product) -> Unit,
        val onSubscribeClicked: (Product) -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ProductCardActions

            if (onProductClicked != other.onProductClicked) return false
            if (onAddToFavoritesClicked != other.onAddToFavoritesClicked) return false
            if (onAddToCartClicked != other.onAddToCartClicked) return false
            return onSubscribeClicked == other.onSubscribeClicked
        }

        override fun hashCode(): Int {
            var result = onProductClicked.hashCode()
            result = 31 * result + onAddToFavoritesClicked.hashCode()
            result = 31 * result + onAddToCartClicked.hashCode()
            result = 31 * result + onSubscribeClicked.hashCode()
            return result
        }
    }

    private val TopBarIconSize: Dp get() = 20.dp

    private const val TagListContentKeyTagList = "TagListContentKeyTagList"

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

    private val AppliedFilterCounterAlignment: Alignment
        get() = BiasAlignment(0.5f, -0.5f)
}
