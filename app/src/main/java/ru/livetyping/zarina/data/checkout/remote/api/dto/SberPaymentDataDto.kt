package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.SberPaymentData
import ru.livetyping.zarina.domain.common.Url

@Serializable
data class SberPaymentDataDto(
    @SerialName("sberUid")
    val sberUid: String? = null,

    @SerialName("sberOrderId")
    val sberOrderId: String? = null,

    @SerialName("formUrl")
    val formUrl: String? = null,
) {
    fun toSberPaymentData(): SberPaymentData {
        checkNotNull(sberUid) { "sberUid is null" }
        checkNotNull(sberOrderId) { "sberOrderId is null" }
        checkNotNull(formUrl) { "formUrl is null" }
        return SberPaymentData(
            sberUid = SberPaymentData.SberUid(sberUid),
            sberOrderId = SberPaymentData.SberOrderId(sberOrderId),
            paymentUrl = Url(formUrl),
        )
    }
}
