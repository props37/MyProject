package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaClubModalBottomSheet
import ru.livetyping.zarina.core.uikit.overlay.ZarinaRefreshingOverlay
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.DeliveryInfo
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.InfoModalBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.PaymentMethodsState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.ui.OrderPlacingScreenComponents.InfoModalBottomSheet
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.ui.OrderPlacingScreenComponents.OrderPlacing
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.ui.OrderPlacingScreenComponents.PaymentMethodSelectorBottomSheet
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarState

@Composable
internal fun OrderPlacingScreen(
    navActions: OrderPlacingNavActions,
    viewModel: OrderPlacingViewModel,
) {
    val state by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val recipient by viewModel.recipient.collectAsStateWithLifecycle()
    val deliveryInfo by viewModel.deliveryInfo.collectAsStateWithLifecycle()
    val cartState by viewModel.cartState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isPullRefreshing by viewModel.isPullRefreshing.collectAsStateWithLifecycle()
    val isPaymentMethodSelectorBottomSheetVisible by viewModel.isPaymentMethodSelectorBottomSheetVisible.collectAsStateWithLifecycle()
    val paymentMethodsState by viewModel.paymentMethodsState.collectAsStateWithLifecycle()
    val isPayButtonLoading by viewModel.isPayButtonLoading.collectAsStateWithLifecycle()
    val infoModalBottomSheetState by viewModel.infoModalBottomSheetState.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel::onBackClicked)

    ScreenContent(
        step = state,
        stepCount = stepCount,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        recipient = recipient,
        onChangeCustomerClicked = viewModel::onChangeCustomerClicked,
        deliveryInfo = deliveryInfo,
        onChangeDeliveryClicked = viewModel::onChangeDeliveryClicked,
        cartState = cartState,
        onCartErrorRefreshClicked = viewModel::onCartErrorRefreshClicked,
        isRefreshing = isRefreshing,
        onPullRefreshTriggered = viewModel::onPullRefreshTriggered,
        isPullRefreshing = isPullRefreshing,
        onIsBonusWriteOffAppliedChanged = viewModel::onIsBonusWriteOffAppliedChanged,
        onBonusCountToWriteOffChanged = viewModel::onBonusCountToWriteOffChanged,
        onIsMyCardAppliedChanged = viewModel::onIsMyCardAppliedChanged,
        onApplyPromoCodeClicked = viewModel::onApplyPromoCodeClicked,
        onRemovePromoCodeClicked = viewModel::onRemovePromoCodeClicked,
        onPromoCodeImeDoneClicked = viewModel::onPromoCodeImeDoneClicked,
        isPaymentMethodSelectorBottomSheetVisible = isPaymentMethodSelectorBottomSheetVisible,
        onPaymentMethodSelectorClicked = viewModel::onPaymentMethodSelectorClicked,
        onPaymentMethodSelectorDismissRequested = viewModel::onPaymentMethodSelectorDismissRequested,
        paymentMethodsState = paymentMethodsState,
        onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
        onPaymentMethodsErrorRefreshClicked = viewModel::onPaymentMethodsErrorRefreshClicked,
        onPayClicked = viewModel::onPayClicked,
        isPayButtonLoading = isPayButtonLoading,
        onInfoButtonClicked = viewModel::onInfoButtonClicked,
        infoModalBottomSheetState = infoModalBottomSheetState,
        onInfoModalBottomSheetClosed = viewModel::onInfoModalBottomSheetClosed,
        onUrlClicked = viewModel::onUrlClicked,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

// Suppress false positive lint checks
@SuppressLint("ComposeMultipleContentEmitters", "ComposeContentEmitterReturningValues")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun ScreenContent(
    step: Int,
    stepCount: Int,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    recipient: Recipient,
    onChangeCustomerClicked: () -> Unit,
    deliveryInfo: DeliveryInfo,
    onChangeDeliveryClicked: () -> Unit,
    cartState: CartState,
    onCartErrorRefreshClicked: () -> Unit,
    isRefreshing: Boolean,
    onPullRefreshTriggered: () -> Unit,
    isPullRefreshing: Boolean,
    onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
    onBonusCountToWriteOffChanged: (Int?) -> Unit,
    onIsMyCardAppliedChanged: (Boolean) -> Unit,
    onApplyPromoCodeClicked: () -> Unit,
    onRemovePromoCodeClicked: () -> Unit,
    onPromoCodeImeDoneClicked: () -> Unit,
    isPaymentMethodSelectorBottomSheetVisible: Boolean,
    onPaymentMethodSelectorClicked: () -> Unit,
    onPaymentMethodSelectorDismissRequested: () -> Unit,
    paymentMethodsState: PaymentMethodsState,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onPaymentMethodsErrorRefreshClicked: () -> Unit,
    onPayClicked: () -> Unit,
    isPayButtonLoading: Boolean,
    onInfoButtonClicked: (OrderPlacingViewModel.InfoButton) -> Unit,
    infoModalBottomSheetState: InfoModalBottomSheetState?,
    onInfoModalBottomSheetClosed: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<OrderPlacingSideEffect>,
    navActions: OrderPlacingNavActions,
) {
    OrderPlacingScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    InfoModalBottomSheet(
        state = infoModalBottomSheetState,
        onDismissRequest = onInfoModalBottomSheetClosed,
    )

    var isZarinaClubBottomSheetVisible by remember { mutableStateOf(false) }
    if (isZarinaClubBottomSheetVisible) {
        ZarinaClubModalBottomSheet(
            onDismissRequest = { isZarinaClubBottomSheetVisible = false },
        )
    }

    PaymentMethodSelectorBottomSheet(
        isVisible = isPaymentMethodSelectorBottomSheetVisible,
        onDismissRequest = onPaymentMethodSelectorDismissRequested,
        paymentMethodsState = paymentMethodsState,
        onPaymentMethodSelected = onPaymentMethodSelected,
        onPaymentMethodsErrorRefreshClicked = onPaymentMethodsErrorRefreshClicked,
        modifier = Modifier.statusBarsPadding(),
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme2.colors.white)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout),
                )
                .imePadding(),
        ) {
            val topBarState = remember(step, stepCount) {
                CheckoutTopBarState(
                    title = Text.Resource(R.string.cart_order_confirmation),
                    checkoutStep = step,
                    checkoutStepCount = stepCount,
                    isBackButtonVisible = true,
                )
            }
            CheckoutTopBar(
                state = topBarState,
                onEvent = {
                    when (it) {
                        CheckoutTopBarEvent.BackClicked -> onBackClicked()
                        CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
                    }
                }
            )

            Box {
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = isPullRefreshing,
                    onRefresh = onPullRefreshTriggered,
                )

                ZarinaPullRefreshIndicator(
                    isRefreshing = isPullRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1f),
                )

                OrderPlacing(
                    recipient = recipient,
                    onChangeCustomerClicked = onChangeCustomerClicked,
                    deliveryInfo = deliveryInfo,
                    onChangeDeliveryClicked = onChangeDeliveryClicked,
                    cartState = cartState,
                    onCartErrorRefreshClicked = onCartErrorRefreshClicked,
                    onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                    onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                    onBonusAccrualClicked = { isZarinaClubBottomSheetVisible = true },
                    onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                    onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                    onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                    onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                    selectedPaymentMethod = remember(paymentMethodsState) {
                        paymentMethodsState.findSelectedPaymentMethod()
                    },
                    onPaymentMethodSelectorClicked = onPaymentMethodSelectorClicked,
                    onPayClicked = onPayClicked,
                    isPayButtonLoading = isPayButtonLoading,
                    onInfoButtonClicked = onInfoButtonClicked,
                    onUrlClicked = { onUrlClicked(Url.create(it)) },
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState),
                )
            }
        }

        AnimatedVisibility(
            visible = isRefreshing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.matchParentSize(),
        ) {
            ZarinaRefreshingOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}
