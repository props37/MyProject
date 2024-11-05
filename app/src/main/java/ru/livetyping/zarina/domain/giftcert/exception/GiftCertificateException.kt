package ru.livetyping.zarina.domain.giftcert.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class GiftCertificateException(message: String) : ValidationException(message)

class EmptyGiftCertificateNumberException(message: String = "Empty gift certificate number") :
    GiftCertificateException(message)

class EmptyGiftCertificateVerificationCodeException(
    message: String = "Empty gift certificate verification code",
) : GiftCertificateException(message)
