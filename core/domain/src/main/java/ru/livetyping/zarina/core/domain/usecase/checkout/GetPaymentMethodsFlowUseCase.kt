package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetPaymentMethodsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<PaymentMethod>>>

    public data class Params(
        val checkoutParams: CheckoutParams,
        val cart: Cart,
    )

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            logger: UseCaseLogger?,
        ): GetPaymentMethodsFlowUseCase {
            return GetPaymentMethodsFlowUseCaseImpl(
                checkoutRepository = checkoutRepository,
                logger = logger,
            )
        }
    }
}
