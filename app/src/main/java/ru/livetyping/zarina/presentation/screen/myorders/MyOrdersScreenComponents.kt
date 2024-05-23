package ru.livetyping.zarina.presentation.screen.myorders

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.presentation.common.component.OrderCard
import ru.livetyping.zarina.presentation.common.component.OrderCardSkeleton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.paging.ZarinaPagingPullRefreshContainer
import ru.livetyping.zarina.presentation.common.component.paging.zarinaPagingAppendItem
import ru.livetyping.zarina.presentation.common.component.paging.zarinaPagingPrependItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.error.rememberErrorState
import ru.livetyping.zarina.util.compose.animation.Crossfade

object MyOrdersScreenComponents {
    
    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.my_orders),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun OrderList(
        orderPagingItems: LazyPagingItems<OrderItem>,
        onOrderClicked: (OrderItem) -> Unit,
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
                            ErrorState.from(loadState.error)
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
        orderPagingItems: LazyPagingItems<OrderItem>,
        onOrderClicked: (OrderItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            if (orderPagingItems.itemCount > 0) {
                val itemModifier = Modifier.fillMaxWidth()
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    zarinaPagingPrependItem(
                        prependLoadState = orderPagingItems.loadState.prepend,
                        onRetryClicked = orderPagingItems::retry,
                    )

                    items(
                        count = orderPagingItems.itemCount,
                        key = orderPagingItems.itemKey { it.id.value },
                        contentType = orderPagingItems.itemContentType {
                            OrderListContentTypeOrderCard
                        },
                    ) { index ->
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

                    zarinaPagingAppendItem(
                        appendLoadState = orderPagingItems.loadState.append,
                        onRetryClicked = orderPagingItems::retry,
                    )
                }
            } else {
                val errorState = rememberErrorState(
                    iconResId = R.drawable.ic_tablet_64,
                    title = stringResource(R.string.no_orders_yet),
                    body = stringResource(R.string.my_orders_no_orders_body),
                    isButtonVisible = false,
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
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier.fillMaxSize(),
        ) {
            items(count = OrderListSkeletonItemCount) { index ->
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

    private const val OrderListSkeletonItemCount = 12

    private const val OrderListContentTypeOrderCard = "OrderListContentTypeOrderCard"
}
