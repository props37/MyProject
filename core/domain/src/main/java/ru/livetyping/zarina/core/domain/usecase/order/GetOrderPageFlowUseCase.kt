package ru.livetyping.zarina.core.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetOrderPageFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Page<List<OrderShort>>>>

    public data class Params(val page: Int)

    public companion object {
        public fun getInstance(
            orderRepository: OrderRepository,
            logger: UseCaseLogger?,
        ): GetOrderPageFlowUseCase {
            return GetOrderPageFlowUseCaseImpl(
                orderRepository = orderRepository,
                logger = logger,
            )
        }
    }
}
