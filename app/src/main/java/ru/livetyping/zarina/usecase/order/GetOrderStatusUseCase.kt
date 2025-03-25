package ru.livetyping.zarina.usecase.order

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.order.OrderRepository
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderStatus
import javax.inject.Inject

class GetOrderStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) : UseCase<GetOrderStatusUseCase.Params, OrderStatus>() {
    override suspend fun execute(params: Params): OrderStatus {
        return orderRepository.getOrderStatus(params.orderId)
    }

    data class Params(val orderId: Order.Id)
}
