package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Delivery

@Serializable
data class DeliveryInfoDto(
    @SerialName("city")
    val cityName: String? = null,
    @SerialName("deliveries")
    val deliveries: List<DeliveryOptionDto>? = null,
) {

    fun toDomain(): Delivery? {
        return if (
            ApiContract.isNotNull(cityName, "cityName")
        ) {
            return Delivery(
                cityName = cityName,
                options = deliveries?.mapNotNull { it.toDomain() }.orEmpty(),
            )
        } else {
            null
        }
    }

}
