package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.SberPaymentData
import ru.livetyping.zarina.core.domain.model.common.Url

@Serializable
internal data class SberPaymentDataDto(
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
            paymentUrl = Url.create(formUrl),
        )
    }
}
