package ru.livetyping.zarina.ui.screen.order.cancellation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.usecase.order.CancelOrderUseCase
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class OrderCancellationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: OrderCancellationInteractor,
) : ViewModel(), SideEffectSource<OrderCancellationViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var cancelOrderJob: Job? = null

    private val orderId: StateFlow<Order.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = ProfileGraph.OrderCancellation.ARG_KEY_ORDER_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "orderId is null" }
            Order.Id(value)
        }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderCancellationScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCancelClicked() {
        if (cancelOrderJob?.isActive == true) return
        cancelOrderJob = viewModelScope.launch {
            operationTracker.track(Operation.CANCEL_ORDER) {
                val params = CancelOrderUseCase.Params(orderId.value)
                interactor.cancelOrder(params)
                    .onSuccess {
                        // TODO: [High] Implement
                    }
                    .onFailure {
                        // TODO: [High] Implement
                    }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: OrderCancellationScreenAction) : SideEffect
    }

    private enum class Operation : OperationKey { CANCEL_ORDER }
}
