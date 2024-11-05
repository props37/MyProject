package ru.livetyping.zarina.domain.giftcert.exception

class GiftCertificateReservedException(message: String = "Gift certificate is reserved") :
    IllegalStateException(message)
