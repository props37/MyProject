package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.component.OrderList
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.component.OrderListTopBar

@Composable
internal fun OrderListScreen(
    navActions: OrderListNavActions,
    viewModel: OrderListViewModel = hiltViewModel(),
) {
    ScreenContent(
        orderPagingDataFlow = viewModel.orderPagingDataFlow,
        onOrderClicked = viewModel::onOrderClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    orderPagingDataFlow: Flow<PagingData<OrderShort>>,
    onOrderClicked: (Order) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<OrderListSideEffect>,
    navActions: OrderListNavActions,
) {
    OrderListScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        OrderListTopBar(onBackClicked = onBackClicked)

        val orderPagingItems = orderPagingDataFlow.collectAsLazyPagingItems()

        OrderList(
            orderPagingItems = orderPagingItems,
            onOrderClicked = onOrderClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
