package ru.livetyping.zarina.core.domain.usecase.order

import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderStatusUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetOrderStatusUseCaseImpl(
    private val orderRepository: OrderRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, OrderStatus>(logger), GetOrderStatusUseCase {

    override suspend fun execute(params: Params): OrderStatus {
        return orderRepository.getOrderStatus(params.orderId)
    }

    override suspend fun invoke(params: Params): Result<OrderStatus> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetOrderStatusUseCaseImpl"
    }
}
