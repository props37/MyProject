package ru.livetyping.zarina.core.domain.usecase.order

import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface CancelOrderUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val orderId: Order.Id)

    public companion object {
        public fun getInstance(
            orderRepository: OrderRepository,
            logger: UseCaseLogger?,
        ): CancelOrderUseCase {
            return CancelOrderUseCaseImpl(
                orderRepository = orderRepository,
                logger = logger,
            )
        }
    }
}
