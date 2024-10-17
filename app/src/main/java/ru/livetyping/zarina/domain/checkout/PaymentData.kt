package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.common.Url

sealed interface PaymentData

data class PaytureInPayPaymentData(
    val data: CardPaymentData,
) : PaymentData

data class PaytureWalletPaymentData(
    val data: CardPaymentData,
) : PaymentData

data class QrPaymentData(
    val paymentUrl: Url,
) : PaymentData
