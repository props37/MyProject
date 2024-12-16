package ru.livetyping.zarina.core.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetOrderFlowUseCaseImpl(
    private val orderRepository: OrderRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, OrderDetailed>(logger), GetOrderFlowUseCase {

    override fun execute(params: Params): Flow<OrderDetailed> {
        return orderRepository.getOrderFlow(params.orderId)
    }

    override fun invoke(params: Params): Flow<Result<OrderDetailed>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetOrderFlowUseCaseImpl"
    }
}
