package ru.livetyping.zarina.core.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderPageFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetOrderPageFlowUseCaseImpl(
    private val orderRepository: OrderRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Page<List<OrderShort>>>(logger), GetOrderPageFlowUseCase {

    override fun execute(params: Params): Flow<Page<List<OrderShort>>> {
        return orderRepository.getOrderPageFlow(params.page)
    }

    override fun invoke(params: Params): Flow<Result<Page<List<OrderShort>>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetOrderPageFlowUseCaseImpl"
    }
}
