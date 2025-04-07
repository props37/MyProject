package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.core.domain.usecase.checkout.GetCourierDeliveryOptionsFlowUseCase.Params

internal class GetCourierDeliveryOptionsFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<DeliveryOption>>(logger), GetCourierDeliveryOptionsFlowUseCase {

    override fun execute(params: Params): Flow<List<DeliveryOption>> {
        return checkoutRepository.getCourierDeliveryOptionsFlow(params.buildingKladrId)
    }

    override fun invoke(params: Params): Flow<Result<List<DeliveryOption>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCourierDeliveryOptionsFlowUseCaseImpl"
    }
}
