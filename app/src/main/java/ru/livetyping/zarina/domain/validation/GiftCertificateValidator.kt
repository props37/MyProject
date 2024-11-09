package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.giftcert.GiftCertificate
import ru.livetyping.zarina.domain.giftcert.exception.EmptyGiftCertificateNumberException
import ru.livetyping.zarina.domain.giftcert.exception.EmptyGiftCertificateVerificationCodeException

class GiftCertificateValidator : Validator<GiftCertificate> {
    override fun validate(input: GiftCertificate) {
        val numberException = if (input.number.value.isBlank()) {
            EmptyGiftCertificateNumberException()
        } else {
            null
        }
        val verificationCodeException = if (input.verificationCode.isBlank()) {
            EmptyGiftCertificateVerificationCodeException()
        } else {
            null
        }

        val validationException =
            ValidationException.from(numberException, verificationCodeException)
        if (validationException != null) throw validationException
    }
}
