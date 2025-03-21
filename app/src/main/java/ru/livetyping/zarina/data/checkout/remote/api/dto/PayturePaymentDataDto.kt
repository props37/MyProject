package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.PayturePaymentData
import ru.livetyping.zarina.domain.common.Url

@Serializable
data class PayturePaymentDataDto(
    @SerialName("uid") 
    val paymentId: String? = null,

    @SerialName("link")
    val paymentUrl: String? = null,
) {
    fun toPayturePaymentData(): PayturePaymentData {
        checkNotNull(paymentId) { "paymentId is null" }
        checkNotNull(paymentUrl) { "paymentUrl is null" }
        return PayturePaymentData(
            paymentId = PayturePaymentData.PaymentId(paymentId),
            paymentUrl = Url(paymentUrl),
        )
    }
}
