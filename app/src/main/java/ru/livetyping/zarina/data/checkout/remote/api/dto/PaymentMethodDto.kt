package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.order.remote.api.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.domain.checkout.PaymentMethod

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
    fun toPaymentMethod(): PaymentMethod {
        checkNotNull(id) { "id is null" }
        checkNotNull(type) { "type is null" }
        checkNotNull(title) { "title is null" }
        checkNotNull(description) { "description is null" }
        return PaymentMethod(
            id = PaymentMethod.Id(id),
            type = type.toPaymentMethodType(),
            title = title,
            description = description,
        )
    }
}
