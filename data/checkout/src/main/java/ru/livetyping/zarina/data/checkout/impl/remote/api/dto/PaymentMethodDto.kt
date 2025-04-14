package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.network.zarina.dto.PaymentMethodTypeDto
import timber.log.Timber

@Serializable
internal data class PaymentMethodDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("code")
    val code: PaymentMethodTypeDto? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("description")
    val description: String? = null,
) {
    fun toPaymentMethod(): PaymentMethod? {
        val paymentType = code?.toPaymentMethodType()
        return if (id != null && paymentType != null && title != null && description != null) {
            PaymentMethod(
                id = PaymentMethod.Id(id.toString()),
                type = paymentType,
                title = title.trim(),
                description = description.trim(),
            )
        } else {
            Timber.tag(TAG).e("Drop $this because it can't be converted to PaymentMethod")
            null
        }
    }

    private companion object {
        private const val TAG = "PaymentMethodDto"
    }
}
