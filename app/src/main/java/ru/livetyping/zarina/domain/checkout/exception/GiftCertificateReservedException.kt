package ru.livetyping.zarina.domain.checkout.exception

class GiftCertificateReservedException(message: String = "Gift certificate is reserved") :
    IllegalStateException(message)
