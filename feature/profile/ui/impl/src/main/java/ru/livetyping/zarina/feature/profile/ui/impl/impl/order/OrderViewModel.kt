package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderState
import javax.inject.Inject

@HiltViewModel
internal class OrderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getOrderFlow: GetOrderFlowUseCase,
) : ViewModel(), SideEffectSource<OrderSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<OrderNavEntry>()
    private val orderId = Order.Id(navEntry.orderId)

    private val orderRequester = FlowRequester(OrderRequest.LOADING) {
        val params = GetOrderFlowUseCase.Params(orderId)
        getOrderFlow(params)
    }

    private val orderResult = orderRequester.flow
        .conflate()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    val orderState: StateFlow<OrderState> = combine(
        orderResult,
        orderRequester.loadingState,
    ) { result, loadingState ->
        val isLoading = loadingState.loadingRequest == OrderRequest.LOADING
        if (isLoading) {
            OrderState.Loading
        } else {
            result.fold(
                onSuccess = { order ->
                    OrderState.Success(order)
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    OrderState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = OrderState.Loading,
    )

    fun onOrderEvent(event: OrderEvent) {
        when (event) {
            OrderEvent.BackClicked -> onBackClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderScreenAction.BackClicked
            emitSideEffect(OrderSideEffect.Navigate(action))
        }
    }

    private enum class OrderRequest : FlowRequest { LOADING, REFRESHING }
}
