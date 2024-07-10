package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddMyCardToCartRequestBody(
    @SerialName("cart_type") 
    val deliveryType: DeliveryTypeDto,
    
    @SerialName("products_first_price_sum")
    val productsFirstPriceSum: Int,
)
