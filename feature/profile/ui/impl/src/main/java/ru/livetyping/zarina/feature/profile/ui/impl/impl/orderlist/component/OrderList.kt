package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorButtonState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.order.OrderCard
import ru.livetyping.zarina.core.uikit.order.OrderCardSkeleton
import ru.livetyping.zarina.core.uikit.paging.ZarinaPagingPullRefreshContainer
import ru.livetyping.zarina.core.uikit.paging.zarinaPagingAppendItem
import ru.livetyping.zarina.core.uikit.paging.zarinaPagingPrependItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun OrderList(
    orderPagingItems: LazyPagingItems<OrderShort>,
    onOrderClicked: (Order) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaPagingPullRefreshContainer(
        loadState = orderPagingItems.loadState,
        onPullRefreshTriggered = orderPagingItems::refresh,
        modifier = modifier,
    ) {
        Crossfade(
            targetState = orderPagingItems.loadState.refresh,
            label = "Orders content",
        ) { loadState ->
            when (loadState) {
                is LoadState.NotLoading -> {
                    OrderListImpl(
                        orderPagingItems = orderPagingItems,
                        onOrderClicked = onOrderClicked,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                LoadState.Loading -> {
                    OrderListSkeleton()
                }

                is LoadState.Error -> {
                    val state = remember(loadState.error) {
                        ZarinaErrorScreenState.from(loadState.error)
                    }

                    ZarinaErrorScreen(
                        state = state,
                        onButtonClicked = orderPagingItems::retry,
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
private fun OrderListImpl(
    orderPagingItems: LazyPagingItems<OrderShort>,
    onOrderClicked: (Order) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (orderPagingItems.itemCount > 0) {
            val itemModifier = Modifier.fillMaxWidth()
            val updatedRetry by rememberUpdatedState { orderPagingItems.retry() }

            LazyColumn(
                contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
                modifier = Modifier.fillMaxSize(),
            ) {
                zarinaPagingPrependItem(
                    prependLoadState = orderPagingItems.loadState.prepend,
                    onRetryClicked = updatedRetry,
                )

                items(
                    count = orderPagingItems.itemCount,
                    key = orderPagingItems.itemKey { it.id.value },
                    contentType = orderPagingItems.itemContentType {
                        OrderListContentTypeOrderCard
                    },
                ) { index ->
                    Column(modifier = Modifier.animateZarinaItem(this)) {
                        val order = orderPagingItems[index]
                        if (order != null) {
                            OrderCard(
                                order = order,
                                onClick = onOrderClicked,
                                modifier = itemModifier,
                            )
                        } else {
                            OrderCardSkeleton(modifier = itemModifier)
                        }

                        if (index < orderPagingItems.itemCount - 1) {
                            ZarinaDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            )
                        }
                    }
                }

                zarinaPagingAppendItem(
                    appendLoadState = orderPagingItems.loadState.append,
                    onRetryClicked = updatedRetry,
                )
            }
        } else {
            val errorState = rememberZarinaErrorScreenState(
                iconResId = RCommon.drawable.ic_tablet_64,
                title = stringResource(R.string.profile_no_orders_yet),
                body = stringResource(R.string.profile_create_order_first),
                buttonState = rememberZarinaErrorButtonState(isButtonVisible = false),
            )
            ZarinaErrorScreen(
                state = errorState,
                onButtonClicked = {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            )
        }
    }
}

@Composable
private fun OrderListSkeleton(
    modifier: Modifier = Modifier,
) {
    val placeholderShimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

    LazyColumn(
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier.fillMaxSize(),
    ) {
        items(count = OrderListSkeletonItemCount) { index ->
            Column(
                modifier = Modifier.animateZarinaItem(this),
            ) {
                OrderCardSkeleton(
                    shimmer = placeholderShimmer,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < OrderListSkeletonItemCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

private const val OrderListSkeletonItemCount = 12

private const val OrderListContentTypeOrderCard = "OrderListContentTypeOrderCard"
