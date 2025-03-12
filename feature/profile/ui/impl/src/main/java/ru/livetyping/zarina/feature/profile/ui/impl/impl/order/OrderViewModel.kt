package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.usecase.order.CancelOrderUseCase
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderCancellationDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderCancellationDialogState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderState
import javax.inject.Inject

@HiltViewModel
internal class OrderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getOrderFlow: GetOrderFlowUseCase,
    private val cancelOrder: CancelOrderUseCase,
) : ViewModel(), SideEffectSource<OrderSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var cancelOrderJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<OrderNavEntry>()
    private val orderId = navEntry.getOrderId()

    private val orderRequester = FlowRequester(OrderRequest.LOADING) {
        val params = GetOrderFlowUseCase.Params(orderId)
        getOrderFlow(params)
    }

    val orderState: StateFlow<OrderState> = combine(
        orderRequester.flow,
        orderRequester.loadingState,
    ) { result, loadingState ->
        val isLoading = loadingState.loadingRequest == OrderRequest.LOADING
        if (isLoading) {
            OrderState.Loading
        } else {
            val isRefreshing = loadingState.loadingRequest == OrderRequest.REFRESHING
            result.fold(
                onSuccess = { order ->
                    OrderState.Success(order, isRefreshing)
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    OrderState.Error(errorState, isRefreshing)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = OrderState.Loading,
    )

    private val isOrderCancellationDialogVisible = MutableStateFlow(false)
    val orderCancellationDialogState: StateFlow<OrderCancellationDialogState> = combine(
        isOrderCancellationDialogVisible,
        operationTracker.ongoingOperationKeys,
    ) { isVisible, ongoingOperations ->
        if (isVisible) {
            OrderCancellationDialogState.Visible(
                isCancelOrderButtonLoading = Operation.CANCEL_ORDER in ongoingOperations,
            )
        } else {
            OrderCancellationDialogState.Hidden
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = OrderCancellationDialogState.Hidden,
    )

    fun onOrderEvent(event: OrderEvent) {
        when (event) {
            OrderEvent.BackClicked -> onBackClicked()
            OrderEvent.PullRefreshTriggered -> orderRequester.request(OrderRequest.REFRESHING)
            OrderEvent.OrderErrorRefreshClicked -> orderRequester.request(OrderRequest.LOADING)
            OrderEvent.CancelOrderClicked -> isOrderCancellationDialogVisible.value = true // TODO: [Top] Test
            OrderEvent.PayForOrderClicked -> TODO() // TODO: [Top] Implement
        }
    }

    fun onOrderCancellationDialogEvent(event: OrderCancellationDialogEvent) {
        when (event) {
            OrderCancellationDialogEvent.CancelOrderClicked -> cancelOrder()
            OrderCancellationDialogEvent.CloseClicked -> {
                cancelOrderJob?.cancel()
                isOrderCancellationDialogVisible.value = false
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderScreenAction.BackClicked
            emitSideEffect(OrderSideEffect.Navigate(action))
        }
    }

    private fun cancelOrder() {
        if (cancelOrderJob?.isActive == true) return

        cancelOrderJob = viewModelScope.launch {
            operationTracker.track(Operation.CANCEL_ORDER) {
                val params = CancelOrderUseCase.Params(orderId)
                cancelOrder(params)
                    .onSuccess {
                        isOrderCancellationDialogVisible.value = false
                        orderRequester.request(OrderRequest.LOADING)
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.profile_order_cancellation_error)
                        val message = ZarinaToastMessage.error(text)
                        emitSideEffect(OrderSideEffect.ShowZarinaToast(message))
                    }
            }
        }
    }

    private enum class OrderRequest : FlowRequest { LOADING, REFRESHING }

    private enum class Operation : OperationKey { CANCEL_ORDER }
}
