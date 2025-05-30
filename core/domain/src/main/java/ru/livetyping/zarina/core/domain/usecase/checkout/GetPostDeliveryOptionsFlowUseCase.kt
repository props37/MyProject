package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetPostDeliveryOptionsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<DeliveryOption>>>

    public data class Params(val buildingFiasId: FiasId)

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            logger: UseCaseLogger?,
        ): GetPostDeliveryOptionsFlowUseCase {
            return GetPostDeliveryOptionsFlowUseCaseImpl(
                checkoutRepository = checkoutRepository,
                logger = logger,
            )
        }
    }
}
