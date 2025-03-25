package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.order.remote.api.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import timber.log.Timber

@Serializable
data class PaymentMethodDto(
    @SerialName("id") 
    val id: Int? = null,

    @SerialName("code")
    val type: PaymentMethodTypeDto? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("description")
    val description: String? = null,
) {
    fun toPaymentMethod(): PaymentMethod? {
        val paymentType = type?.toPaymentMethodType()
        return if (id != null && paymentType != null && title != null && description != null) {
            PaymentMethod(
                id = PaymentMethod.Id(id),
                type = paymentType,
                title = title.trim(),
                description = description.trim(),
            )
        } else {
            Timber.e("Drop $this because it can't be converted to PaymentMethod")
            null
        }
    }
}
