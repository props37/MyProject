package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplyMyCardToCartRequestBody(
    @SerialName("cart_type") 
    val cartType: CartTypeDto,

    @SerialName("products_first_price_sum")
    val productsFirstPriceSum: Int,
)
