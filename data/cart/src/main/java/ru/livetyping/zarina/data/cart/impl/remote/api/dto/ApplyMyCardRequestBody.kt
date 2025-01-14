package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApplyMyCardRequestBody(
    @SerialName("cart_type")
    val cartType: CartTypeDto,

    @SerialName("products_first_price_sum")
    val productsFirstPriceSum: Int,
)
