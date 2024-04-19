package ru.livetyping.zarina.usecase.order

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.order.OrderRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderDetails
import javax.inject.Inject

class GetOrderFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
) : FlowUseCase<GetOrderFlowUseCase.Params, OrderDetails>(dispatcher) {

    override fun execute(params: Params): Flow<OrderDetails> {
        return orderRepository.getOrderFlow(params.orderId)
    }

    data class Params(val orderId: Order.Id)
}
