package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceDto(
    @SerialName("common_price")
    val commonPrice: Long? = null,

    @SerialName("has_discount")
    val hasDiscount: Boolean? = null,

    @SerialName("discount")
    val discountPercent: Int? = null,

    @SerialName("discount_price")
    val discountPrice: Long? = null,
)
