package ru.livetyping.zarina.core.domain.usecase.order

import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.usecase.order.CancelOrderUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class CancelOrderUseCaseImpl(
    private val orderRepository: OrderRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), CancelOrderUseCase {

    override suspend fun execute(params: Params) {
        orderRepository.cancelOrder(params.orderId)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "CancelOrderUseCaseImpl"
    }
}
