package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.order.remote.api.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import timber.log.Timber

@Serializable
data class DeliveryMethodDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("type")
    val type: DeliveryMethodTypeDto? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,
) {
    fun toDeliveryMethod(): DeliveryMethod? {
        val deliveryMethodType = type?.toDeliveryMethodType()
        return if (id != null && deliveryMethodType != null && name != null && description != null) {
            DeliveryMethod(
                id = DeliveryMethod.Id(id),
                type = deliveryMethodType,
                name = name,
                description = description?.takeIf { it.isNotBlank() },
            )
        } else {
            Timber.e("Drop $this because it can't be converted to DeliveryMethod")
            null
        }
    }
}
