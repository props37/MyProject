package ru.livetyping.zarina.core.domain.usecase.order

import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetOrderStatusUseCase {
    public suspend operator fun invoke(params: Params): Result<OrderStatus>

    public data class Params(val orderId: Order.Id)

    public companion object {
        public fun getInstance(
            orderRepository: OrderRepository,
            logger: UseCaseLogger?,
        ): GetOrderStatusUseCase {
            return GetOrderStatusUseCaseImpl(
                orderRepository = orderRepository,
                logger = logger,
            )
        }
    }
}
