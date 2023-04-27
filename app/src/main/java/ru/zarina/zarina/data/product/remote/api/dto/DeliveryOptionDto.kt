package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Delivery

@Serializable
data class DeliveryOptionDto(
    @SerialName("type")
    val type: Type? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("estimation_days")
    val estimationDays: Int? = null,
) {

    fun toDomain(): Delivery.Option? {
        val type = type?.toDomain()
        return if (
            ApiContract.isNotNull(type, "type")
            && ApiContract.isNotNull(title, "title")
            && ApiContract.isNotNull(estimationDays, "estimationDays")
        ) {
            Delivery.Option(
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
            "express" -> Delivery.Option.Type.EXPRESS
            "post" -> Delivery.Option.Type.POST
            "pickup" -> Delivery.Option.Type.PICKUP
            "retail" -> Delivery.Option.Type.RETAIL
            else -> null
        }
    }

}
