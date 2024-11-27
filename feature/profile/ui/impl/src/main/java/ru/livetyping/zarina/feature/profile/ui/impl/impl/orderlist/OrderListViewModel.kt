package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.paging.OrderPager
import javax.inject.Inject

@HiltViewModel
internal class OrderListViewModel @Inject constructor(
    orderPager: OrderPager,
) : ViewModel(), SideEffectSource<OrderListSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val orderPagingDataFlow: Flow<PagingData<OrderShort>> = orderPager
        .getOrderPagingDataFlow()
        .cachedIn(viewModelScope)

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderListScreenAction.ScreenClosed
            emitSideEffect(OrderListSideEffect.Navigate(action))
        }
    }

    fun onOrderClicked(order: Order) {
        TODO()
        // TODO: [Top] Implement
    }
}
