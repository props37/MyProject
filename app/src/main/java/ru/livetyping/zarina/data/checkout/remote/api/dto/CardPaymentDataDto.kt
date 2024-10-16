package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.CardPaymentData
import ru.livetyping.zarina.domain.common.Url

@Serializable
data class CardPaymentDataDto(
    @SerialName("uid") 
    val paymentId: String? = null,

    @SerialName("link")
    val paymentUrl: String? = null,
) {
    fun toCardPaymentData(): CardPaymentData {
        checkNotNull(paymentId) { "paymentId is null" }
        checkNotNull(paymentUrl) { "paymentUrl is null" }
        return CardPaymentData(
            paymentId = CardPaymentData.PaymentId(paymentId),
            paymentUrl = Url(paymentUrl),
        )
    }
}
