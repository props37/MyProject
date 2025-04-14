package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutStep
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface CheckoutUseCase {
    public operator fun invoke(params: Params): Flow<Result<CheckoutStep>>

    public data class Params(
        val cart: Cart,
        val paymentMethod: PaymentMethod,
        val availablePaymentMethods: List<PaymentMethod>,
        val checkoutParams: CheckoutParams,
    )

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            orderRepository: OrderRepository,
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): CheckoutUseCase {
            return CheckoutUseCaseImpl(
                checkoutRepository = checkoutRepository,
                orderRepository = orderRepository,
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
