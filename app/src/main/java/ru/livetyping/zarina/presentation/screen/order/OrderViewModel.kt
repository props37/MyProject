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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.presentation.common.datafetchinginfo.DataFetchingInfoHolder
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.screen.order.OrderViewModel.SideEffect
import ru.livetyping.zarina.usecase.order.GetOrderFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState

@HiltViewModel(assistedFactory = OrderViewModel.Factory::class)
class OrderViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    savedStateHandle: SavedStateHandle,
    private val interactor: OrderInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

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

    private val orderFetchingInfoHolder = DataFetchingInfoHolder<OrderFetchingType>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val orderResult: StateFlow<Result<OrderDetails>?> = combine(
        orderId,
        orderFetchingInfoHolder.fetchingRequests,
    ) { orderId, _ ->
        val params = GetOrderFlowUseCase.Params(orderId)
        interactor.getOrderFlow(params)
    }
        .flatMapLatest { it }
        .onEach { orderFetchingInfoHolder.completeFetching() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val orderState: StateFlow<OrderState> = combine(
        orderFetchingInfoHolder.fetchingType,
        orderResult,
    ) { orderFetchingType, orderResult ->
        if (orderFetchingType == OrderFetchingType.LOADING || orderResult == null) {
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

    val isRefreshing: StateFlow<Boolean> = orderFetchingInfoHolder.fetchingType.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it == OrderFetchingType.REFRESHING }

    init {
        orderFetchingInfoHolder.requestFetching(OrderFetchingType.LOADING)

        handleOrderCancellationResult()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onRefreshTriggered() {
        orderFetchingInfoHolder.requestFetching(OrderFetchingType.REFRESHING)
    }

    fun onOrderErrorRefreshClicked() {
        orderFetchingInfoHolder.requestFetching(OrderFetchingType.LOADING)
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
                key = ProfileGraph.OrderCancellation.RESULT_KEY,
            ) {
                orderFetchingInfoHolder.requestFetching(OrderFetchingType.LOADING)
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: OrderScreenAction) : SideEffect
    }

    @Stable
    sealed class OrderState {
        data object Loading : OrderState()

        @Immutable
        data class Order(val order: OrderDetails) : OrderState()

        @Immutable
        data class Error(val state: ErrorState) : OrderState()
    }

    private enum class OrderFetchingType { LOADING, REFRESHING }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): OrderViewModel
    }
}
