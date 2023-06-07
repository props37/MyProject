package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceFilterDto(
    @SerialName("min")
    val min: Int? = null,
    @SerialName("max")
    val max: Int? = null,
)
