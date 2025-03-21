package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.network.zarina.dto.DeliveryMethodTypeDto

@Serializable
internal data class DeliveryMethodDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("type")
    val type: DeliveryMethodTypeDto? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,
) {
    fun toDeliveryMethod(): DeliveryMethod {
        checkNotNull(id) { "id is null" }
        checkNotNull(type) { "type is null" }
        checkNotNull(name) { "name is null" }
        return DeliveryMethod(
            id = DeliveryMethod.Id(id),
            type = type.toDeliveryMethodType(),
            name = name,
            description = description?.takeIf { it.isNotBlank() },
        )
    }
}
