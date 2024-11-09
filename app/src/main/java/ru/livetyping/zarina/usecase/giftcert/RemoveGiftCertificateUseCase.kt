package ru.livetyping.zarina.usecase.giftcert

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.domain.order.PaymentMethodType
import javax.inject.Inject

class RemoveGiftCertificateUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
) : UseCase<RemoveGiftCertificateUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        checkoutRepository.removeGiftCertificate(
            paymentMethodType = params.paymentMethodType,
        )
    }

    data class Params(val paymentMethodType: PaymentMethodType)
}
