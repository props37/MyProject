package ru.livetyping.zarina.usecase.giftcert

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.giftcert.GiftCertificate
import ru.livetyping.zarina.domain.validation.GiftCertificateValidator
import javax.inject.Inject

class ApplyGiftCertificateUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
) : UseCase<ApplyGiftCertificateUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val certificate = params.giftCertificate
        validateGiftCertificate(certificate)
        checkoutRepository.applyGiftCertificate(
            giftCertificate = certificate,
            cartFinalPrice = params.cartFinalPrice,
            cartType = params.cartType,
        )
    }

    private fun validateGiftCertificate(certificate: GiftCertificate) {
        val validator = GiftCertificateValidator()
        validator.validate(certificate)
    }

    data class Params(
        val giftCertificate: GiftCertificate,
        val cartFinalPrice: Int,
        val cartType: CartType,
    )
}
