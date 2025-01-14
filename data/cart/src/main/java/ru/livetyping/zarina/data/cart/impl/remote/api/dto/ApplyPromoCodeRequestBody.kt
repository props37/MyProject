package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApplyPromoCodeRequestBody(
    @SerialName("promocode")
    val promoCode: String? = null,
)
