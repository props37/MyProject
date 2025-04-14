package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PayturePaymentData
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class PayturePaymentDataDto(
    @SerialName("uid")
    val uid: String? = null,

    @SerialName("link")
    val link: String? = null,
) {
    fun toPayturePaymentData(): PayturePaymentData {
        checkPropertyNotNull(uid) { ::uid }
        checkPropertyNotNull(link) { ::link }
        return PayturePaymentData(
            paymentId = PayturePaymentData.PaymentId(uid),
            paymentUrl = Url.create(link),
        )
    }
}
