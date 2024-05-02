package ru.livetyping.zarina.ui.screen.myorders

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.myorders.MyOrdersScreenComponents.OrderList
import ru.livetyping.zarina.ui.screen.myorders.MyOrdersScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.myorders.MyOrdersViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun MyOrdersScreen(
    navigate: (MyOrdersScreenAction) -> Unit,
    viewModel: MyOrdersViewModel = hiltViewModel(),
) {
    ScreenContent(
        orderPagingDataFlow = viewModel.orderPagingDataFlow,
        onOrderClicked = viewModel::onOrderClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    orderPagingDataFlow: Flow<PagingData<OrderItem>>,
    onOrderClicked: (OrderItem) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (MyOrdersScreenAction) -> Unit,
) {
    MyOrdersScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
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
        TopBar(onBackClicked = onBackClicked)

        val orderPagingItems = orderPagingDataFlow.collectAsLazyPagingItems()

        OrderList(
            orderPagingItems = orderPagingItems,
            onOrderClicked = onOrderClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
