package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetPickupPointsFlowUseCase {
    public operator fun invoke(): Flow<Result<List<PickupPointShort>>>

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
