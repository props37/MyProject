package ru.livetyping.zarina.core.domain.model.giftcert.exception

public open class GiftCertificateException(message: String) : Exception(message)

public class EmptyGiftCertificateNumberException(message: String = "Empty gift certificate number") :
    GiftCertificateException(message)

public class EmptyGiftCertificateVerificationCodeException(
    message: String = "Empty gift certificate verification code",
) : GiftCertificateException(message)

public class GiftCertificateReservedException(message: String = "Gift certificate is reserved") :
    GiftCertificateException(message)
