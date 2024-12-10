package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AddProductToCartRequestBody(
    @SerialName("barcode")
    val barcode: String,
    
    @SerialName("quantity")
    val count: Int,
)
