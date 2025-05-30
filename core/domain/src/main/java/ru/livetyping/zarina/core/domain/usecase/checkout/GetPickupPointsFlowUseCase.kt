package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetPickupPointsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<PickupPointShort>>>

    public data class Params(val cityFiasId: FiasId? = null)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            checkoutRepository: CheckoutRepository,
            logger: UseCaseLogger?,
        ): GetPickupPointsFlowUseCase {
            return GetPickupPointsFlowUseCaseImpl(
                userRepository = userRepository,
                checkoutRepository = checkoutRepository,
                logger = logger,
            )
        }
    }
}
