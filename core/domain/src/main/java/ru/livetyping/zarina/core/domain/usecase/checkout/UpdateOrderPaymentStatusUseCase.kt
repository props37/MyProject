package ru.livetyping.zarina.core.domain.usecase.checkout

import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface UpdateOrderPaymentStatusUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val orderId: Order.Id,
        val paymentMethodType: PaymentMethodType,
    )

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            logger: UseCaseLogger?,
        ): UpdateOrderPaymentStatusUseCase {
            return UpdateOrderPaymentStatusUseCaseImpl(
                checkoutRepository = checkoutRepository,
                logger = logger,
            )
        }
    }
}
