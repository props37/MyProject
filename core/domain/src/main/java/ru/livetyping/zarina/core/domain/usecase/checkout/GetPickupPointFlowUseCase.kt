package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetPickupPointFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<PickupPointDetailed>>

    public data class Params(val pickupPointId: PickupPoint.Id)

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetPickupPointFlowUseCase {
            return GetPickupPointFlowUseCaseImpl(
                checkoutRepository = checkoutRepository,
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
