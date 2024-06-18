package ru.livetyping.zarina.presentation.screen.myorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.myorders.MyOrdersViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    interactor: MyOrdersInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val orderPagingDataFlow: Flow<PagingData<OrderItem>> = interactor.orderPager
        .getOrderPagingDataFlow()
        .cachedIn(viewModelScope)

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = MyOrdersScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOrderClicked(order: OrderItem) {
        navigationThrottler.throttle {
            val action = MyOrdersScreenAction.OrderClicked(order.id)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: MyOrdersScreenAction) : SideEffect
    }
}
