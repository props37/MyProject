package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetDeliveryMethodsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<DeliveryMethod>>>

    public data class Params(public val cartType: CartType)

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetDeliveryMethodsFlowUseCase {
            return GetDeliveryMethodsFlowUseCaseImpl(
                checkoutRepository = checkoutRepository,
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
