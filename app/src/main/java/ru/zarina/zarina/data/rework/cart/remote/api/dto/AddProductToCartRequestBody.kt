package ru.zarina.zarina.data.rework.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddProductToCartRequestBody(
    @SerialName("barcode") 
    val barcode: String,
    
    @SerialName("quantity")
    val count: Int,
)
