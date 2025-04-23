package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.OrderConfirmedState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.ui.OrderConfirmedContent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.ui.TopBar

@Composable
internal fun OrderConfirmedScreen(
    navActions: OrderConfirmedNavActions,
    viewModel: OrderConfirmedViewModel = hiltViewModel(),
) {
    val orderConfirmedState by viewModel.orderConfirmedState.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel::onBackClicked)

    ScreenContent(
        orderConfirmedState = orderConfirmedState,
        onReturnToHomeClicked = viewModel::onReturnToHomeClicked,
        onPayClicked = viewModel::onPayClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    orderConfirmedState: OrderConfirmedState,
    onReturnToHomeClicked: () -> Unit,
    onPayClicked: () -> Unit,
    sideEffects: Flow<OrderConfirmedSideEffect>,
    navActions: OrderConfirmedNavActions,
) {
    OrderConfirmedScreenBehavior(
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
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(onCloseClicked = onReturnToHomeClicked)

        OrderConfirmedContent(
            state = orderConfirmedState,
            onReturnToHomeClicked = onReturnToHomeClicked,
            onPayClicked = onPayClicked,
            windowInsetsProvider = { WindowInsets.safeDrawing },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )
    }
}
