package ru.livetyping.zarina.usecase.checkout

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.PaymentMethodType
import javax.inject.Inject

class UpdateOrderPaymentStatusUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
) : UseCase<UpdateOrderPaymentStatusUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val orderId = params.orderId
        val paymentMethodType = params.paymentMethodType
        checkoutRepository.updateOrderPaymentStatus(orderId, paymentMethodType)
    }

    data class Params(
        val orderId: Order.Id,
        val paymentMethodType: PaymentMethodType,
    )
}
