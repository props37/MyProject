package ru.livetyping.zarina.data.old.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.DeliveryAvailability

@Serializable
data class DeliveryOptionDto(
    @SerialName("type")
    val type: Type? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("estimation_days")
    val estimationDays: Int? = null,
) {

    fun toDomain(): DeliveryAvailability.Option? {
        val type = type?.toDomain()
        return if (
            ApiContract.isNotNull(type, "type")
            && ApiContract.isNotNull(title, "title")
            && ApiContract.isNotNull(estimationDays, "estimationDays")
        ) {
            DeliveryAvailability.Option(
                type = type,
                name = title,
                estimatedTimeDays = estimationDays
            )
        } else {
            null
        }
    }

    @Serializable
    @JvmInline
    value class Type(val value: String) {
        fun toDomain() = when (this.value) {
            "express" -> DeliveryAvailability.Option.Type.EXPRESS
            "post" -> DeliveryAvailability.Option.Type.POST
            "pickup" -> DeliveryAvailability.Option.Type.PICKUP
            "retail" -> DeliveryAvailability.Option.Type.RETAIL
            else -> null
        }
    }

}
