package ru.livetyping.zarina.domain.giftcert.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class GiftCertificateValidationException(message: String) : ValidationException(message)

class EmptyGiftCertificateNumberException(message: String = "Empty gift certificate number") :
    GiftCertificateValidationException(message)

class EmptyGiftCertificateVerificationCodeException(
    message: String = "Empty gift certificate verification code",
) : GiftCertificateValidationException(message)

class GiftCertificateReservedException(message: String = "Gift certificate is reserved") :
    GiftCertificateValidationException(message)
