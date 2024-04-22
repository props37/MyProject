package ru.livetyping.zarina.ui.screen.order

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.screen.order.OrderViewModel.OrderState
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.animation.Crossfade

object OrderScreenComponents {

    @Composable
    fun TopBar(
        orderNumber: Order.Number?,
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
                AnimatedContent(
                    targetState = orderNumber,
                    transitionSpec = {
                        AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
                    },
                    contentAlignment = Alignment.Center,
                    label = "Order number",
                ) { number ->
                    if (number != null) {
                        Text(
                            text = stringResource(R.string.order_number, number.value),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        ZarinaSkeleton(modifier = Modifier.size(120.dp, 16.dp))
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Order(
        orderState: OrderState,
        isRefreshing: Boolean,
        onPullRefreshTriggered: () -> Unit,
        onOrderErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = onPullRefreshTriggered,
            )

            ZarinaPullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )

            Crossfade(
                targetState = orderState,
                contentKey = {
                    when (it) {
                        // TODO: [High] Extract
                        is OrderState.Order -> "ContentKeyOrder"
                        is OrderState.Error, OrderState.Loading -> it
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
            ) { state ->
                when (state) {
                    is OrderState.Order -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {

                        }
                    }

                    OrderState.Loading -> {
                        // TODO: [High] Implement
                    }

                    is OrderState.Error -> {
                        ZarinaErrorScreen(
                            state = state.state,
                            onButtonClicked = onOrderErrorRefreshClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                        )
                    }
                }
            }
        }
    }
}
