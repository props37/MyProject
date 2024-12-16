package ru.livetyping.zarina.core.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetOrderFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<OrderDetailed>>

    public data class Params(val orderId: Order.Id)

    public companion object {
        public fun getInstance(
            orderRepository: OrderRepository,
            logger: UseCaseLogger?,
        ): GetOrderFlowUseCase {
            return GetOrderFlowUseCaseImpl(
                orderRepository = orderRepository,
                logger = logger,
            )
        }
    }
}
