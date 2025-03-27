package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.network.zarina.dto.DeliveryMethodTypeDto
import timber.log.Timber

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
    fun toDeliveryMethod(): DeliveryMethod? {
        val deliveryMethodType = type?.toDeliveryMethodType()
        return if (id != null && deliveryMethodType != null && name != null) {
            DeliveryMethod(
                id = DeliveryMethod.Id(id),
                type = deliveryMethodType,
                name = name,
                description = description?.takeIf { it.isNotBlank() },
            )
        } else {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to DeliveryMethod")
            null
        }
    }

    private companion object {
        private const val TAG = "DeliveryMethodDto"
    }
}
