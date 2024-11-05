package ru.livetyping.zarina.usecase.giftcert

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.giftcert.exception.EmptyGiftCertificateNumberException
import ru.livetyping.zarina.domain.giftcert.exception.EmptyGiftCertificateVerificationCodeException
import javax.inject.Inject

class ApplyGiftCertificateUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
) : UseCase<ApplyGiftCertificateUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val certNumber = params.certificateNumber
        val certVerificationCode = params.certificateVerificationCode
        validateGiftCertificate(
            certificateNumber = certNumber,
            certificateVerificationCode = certVerificationCode,
        )
        checkoutRepository.applyGiftCertificate(
            certificateNumber = certNumber,
            certificateVerificationCode = certVerificationCode,
            cartTotalPrice = params.cartTotalPrice,
            cartType = params.cartType,
        )
    }

    private fun validateGiftCertificate(
        certificateNumber: String,
        certificateVerificationCode: String,
    ) {
        val numberException = if (certificateNumber.isBlank()) {
            EmptyGiftCertificateNumberException()
        } else {
            null
        }
        val verificationCodeException = if (certificateVerificationCode.isBlank()) {
            EmptyGiftCertificateVerificationCodeException()
        } else {
            null
        }

        val validationException =
            ValidationException.from(numberException, verificationCodeException)
        if (validationException != null) throw validationException
    }

    data class Params(
        val certificateNumber: String,
        val certificateVerificationCode: String,
        val cartTotalPrice: Int,
        val cartType: CartType,
    )
}
