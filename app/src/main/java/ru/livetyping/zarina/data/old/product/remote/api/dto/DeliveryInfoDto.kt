package ru.livetyping.zarina.data.old.product.remote.api.dto

import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.DeliveryAvailability

@Serializable
data class DeliveryInfoDto(
    @SerialName("city")
    val cityName: String? = null,
    @SerialName("deliveries")
    val deliveries: List<DeliveryOptionDto>? = null,
) {

    fun toDomain(): DeliveryAvailability? {
        return if (
            ApiContract.isNotNull(cityName, "cityName")
        ) {
            return DeliveryAvailability(
                cityName = cityName,
                options = deliveries?.mapNotNull { it.toDomain() }.orEmpty().toImmutableList(),
            )
        } else {
            null
        }
    }

}
