package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.order.remote.api.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.domain.checkout.DeliveryMethod

@Serializable
data class DeliveryMethodDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("type")
    val type: DeliveryMethodTypeDto? = null,

    @SerialName("name")
    val name: String? = null,
) {
    fun toDeliveryMethod(): DeliveryMethod {
        checkNotNull(id) { "id is null" }
        checkNotNull(type) { "type is null" }
        checkNotNull(name) { "name is null" }
        return DeliveryMethod(
            id = DeliveryMethod.Id(id),
            type = type.toDeliveryMethodType(),
            name = name,
        )
    }
}
