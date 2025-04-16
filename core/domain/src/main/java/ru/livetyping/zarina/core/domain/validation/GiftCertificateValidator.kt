package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.model.giftcert.exception.EmptyGiftCertificateNumberException
import ru.livetyping.zarina.core.domain.model.giftcert.exception.EmptyGiftCertificateVerificationCodeException
import ru.livetyping.zarina.core.domain.model.giftcert.exception.GiftCertificateException

/**
 * @throws GiftCertificateException
 * @throws CombinedValidationException
 */
public class GiftCertificateValidator : Validator<GiftCertificate> {
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

        val exceptions = listOfNotNull(numberException, verificationCodeException)
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
        }
    }
}
