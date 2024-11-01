package ru.livetyping.zarina.presentation.screen.order

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.screen.order.OrderViewModel.SideEffect
import ru.livetyping.zarina.usecase.order.GetOrderFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState

@HiltViewModel(assistedFactory = OrderViewModel.Factory::class)
class OrderViewModel @AssistedInject constructor(
    @Assisted
    private val orderCancellationResultFlow: StateFlow<ProfileGraph.OrderCancellation.Result?>,
    savedStateHandle: SavedStateHandle,
    private val interactor: OrderInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val orderId: StateFlow<Order.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = ProfileGraph.Order.ARG_KEY_ORDER_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "orderId is null" }
            Order.Id(value)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val orderRequester = FlowRequester(OrderRequest.LOADING) {
        orderId.flatMapLatest { orderId ->
            val params = GetOrderFlowUseCase.Params(orderId)
            interactor.getOrderFlow(params)
        }
    }

    private val orderResult: StateFlow<Result<OrderDetails>?> = orderRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val orderState: StateFlow<OrderState> = combine(
        orderRequester.loadingState,
        orderResult,
    ) { loadingState, orderResult ->
        val isLoading = loadingState is FlowRequester.LoadingState.Loading
                && loadingState.request == OrderRequest.LOADING
        if (isLoading || orderResult == null) {
            OrderState.Loading
        } else {
            orderResult.fold(
                onSuccess = { order ->
                    OrderState.Order(order)
                },
                onFailure = { throwable ->
                    val errorState = ErrorState.from(throwable)
                    OrderState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = OrderState.Loading,
    )

    val isRefreshing: StateFlow<Boolean> = orderRequester.loadingState.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) {
        it is FlowRequester.LoadingState.Loading && it.request == OrderRequest.REFRESHING
    }

    init {
        handleOrderCancellationResult()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onRefreshTriggered() {
        orderRequester.request(OrderRequest.REFRESHING)
    }

    fun onOrderErrorRefreshClicked() {
        orderRequester.request(OrderRequest.LOADING)
    }

    fun onPayForOrderClicked() {
        val orderPaymentUrl = orderResult.value?.getOrNull()?.paymentUrl
        if (orderPaymentUrl != null) {
            navigationThrottler.throttle {
                val action = OrderScreenAction.PayForOrderClicked(orderPaymentUrl)
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            val messageText = Text.Resource(R.string.something_went_wrong)
            val message = ZarinaToastMessage.error(messageText)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
        }
    }

    fun onCancelOrderClicked() {
        navigationThrottler.throttle {
            val action = OrderScreenAction.CancelOrderClicked(orderId.value)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun handleOrderCancellationResult() {
        viewModelScope.launch {
            screenResultHandler.handle<ProfileGraph.OrderCancellation.Result>(
                resultFlow = orderCancellationResultFlow,
                key = KEY_RESULT_ORDER_CANCELLATION,
            ) {
                orderRequester.request(OrderRequest.LOADING)
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: OrderScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Stable
    sealed class OrderState {
        data object Loading : OrderState()

        @Immutable
        data class Order(val order: OrderDetails) : OrderState()

        @Immutable
        data class Error(val state: ErrorState) : OrderState()
    }

    private enum class OrderRequest : FlowRequester.Request { LOADING, REFRESHING }

    @AssistedFactory
    interface Factory {
        fun create(
            orderCancellationResultFlow: StateFlow<ProfileGraph.OrderCancellation.Result?>,
        ): OrderViewModel
    }

    companion object {
        private const val KEY_RESULT_ORDER_CANCELLATION = "result_order_cancellation"
    }
}
