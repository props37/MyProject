package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaClubModalBottomSheet
import ru.livetyping.zarina.presentation.common.component.overlay.ZarinaRefreshingOverlay
import ru.livetyping.zarina.presentation.common.component.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.cart.model.CartState
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingScreenComponents.OrderPlacing
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingScreenComponents.PaymentMethodSelectorBottomSheet
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.DeliveryInfo
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.PaymentMethodsState
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutOrderPlacingScreen(
    viewModel: CheckoutOrderPlacingViewModel,
    navigate: (CheckoutOrderPlacingScreenAction) -> Unit,
) {
    val state by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val customer by viewModel.customer.collectAsStateWithLifecycle()
    val deliveryInfo by viewModel.deliveryInfo.collectAsStateWithLifecycle()
    val cartState by viewModel.cartState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isPullRefreshing by viewModel.isPullRefreshing.collectAsStateWithLifecycle()
    val isPaymentMethodSelectorBottomSheetVisible by viewModel.isPaymentMethodSelectorBottomSheetVisible.collectAsStateWithLifecycle()
    val paymentMethodsState by viewModel.paymentMethodsState.collectAsStateWithLifecycle()
    val isPayButtonLoading by viewModel.isPayButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        step = state,
        stepCount = stepCount,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        customer = customer,
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
        onUrlClicked = viewModel::onUrlClicked,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
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
    customer: Customer,
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
    onUrlClicked: (Url) -> Unit,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutOrderPlacingScreenAction) -> Unit,
) {
    CheckoutOrderPlacingScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    BackHandler(onBack = onBackClicked)

    var isZarinaClubBottomSheetVisible by remember { mutableStateOf(false) }
    ZarinaClubModalBottomSheet(
        isVisible = isZarinaClubBottomSheetVisible,
        onDismissRequest = { isZarinaClubBottomSheetVisible = false },
        onUrlClicked = onUrlClicked,
    )

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
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout),
                )
                .imePadding(),
        ) {
            CheckoutComponents.TopBar(
                title = stringResource(R.string.order_confirmation),
                step = step,
                stepCount = stepCount,
                isBackButtonVisible = true,
                onBackClicked = onBackClicked,
                onCloseClicked = onCloseClicked,
            )

            Box {
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = isPullRefreshing,
                    onRefresh = onPullRefreshTriggered,
                )

                ZarinaPullRefreshIndicator(
                    refreshing = isPullRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1f),
                )

                OrderPlacing(
                    customer = customer,
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
                    onUrlClicked = { onUrlClicked(Url(it)) },
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

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // Add preview
    }
}
