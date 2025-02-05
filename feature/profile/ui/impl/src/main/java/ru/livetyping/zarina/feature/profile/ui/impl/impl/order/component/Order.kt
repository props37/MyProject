package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderState

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Order(
    state: OrderState,
    onEvent: (OrderEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = { onEvent(OrderEvent.PullRefreshTriggered) },
        )

        ZarinaPullRefreshIndicator(
            isRefreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
        )

        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is OrderState.Success -> OrderContentKey.Success
                    OrderState.Loading -> it
                    is OrderState.Error -> it
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { state ->
            when (state) {
                is OrderState.Success -> {
                    OrderSuccess(
                        order = state.order,
                        onPayForOrderClicked = { onEvent(OrderEvent.PayForOrderClicked) },
                        onCancelOrderClicked = { onEvent(OrderEvent.CancelOrderClicked) },
                    )
                }

                OrderState.Loading -> {
                    OrderLoading(modifier = Modifier.fillMaxSize())
                }

                is OrderState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = { onEvent(OrderEvent.OrderErrorRefreshClicked) },
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

private enum class OrderContentKey { Success }
