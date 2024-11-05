package ru.livetyping.zarina.usecase.giftcert

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.domain.cart.CartType
import javax.inject.Inject

class ApplyGiftCertificateUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
) : UseCase<ApplyGiftCertificateUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        // TODO: [High] Add validation
        checkoutRepository.applyGiftCertificate(
            certificateNumber = params.certificateNumber,
            certificateVerificationCode = params.certificateVerificationCode,
            cartTotalPrice = params.cartTotalPrice,
            cartType = params.cartType,
        )
    }

    data class Params(
        val certificateNumber: String,
        val certificateVerificationCode: String,
        val cartTotalPrice: Int,
        val cartType: CartType,
    )
}
