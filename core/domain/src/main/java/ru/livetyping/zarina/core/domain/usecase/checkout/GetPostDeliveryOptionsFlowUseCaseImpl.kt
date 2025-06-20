package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPostDeliveryOptionsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetPostDeliveryOptionsFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<DeliveryOption>>(logger), GetPostDeliveryOptionsFlowUseCase {

    override fun execute(params: Params): Flow<List<DeliveryOption>> {
        return checkoutRepository.getPostDeliveryOptionsFlow(params.buildingFiasId)
    }

    override fun invoke(params: Params): Flow<Result<List<DeliveryOption>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetPostDeliveryOptionsFlowUseCaseImpl"
    }
}
