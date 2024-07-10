package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplyPromoCodeRequestBody(
    @SerialName("promocode")
    val promoCode: String? = null,
)
