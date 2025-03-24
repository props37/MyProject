package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetPickupStoresFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<PickupStore>>>

    public data class Params(val deliveryMethodType: DeliveryMethodType)

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetPickupStoresFlowUseCase {
            return GetPickupStoresFlowUseCaseImpl(
                checkoutRepository = checkoutRepository,
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
