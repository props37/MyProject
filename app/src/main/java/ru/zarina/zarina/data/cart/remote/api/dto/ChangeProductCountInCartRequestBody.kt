package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeProductCountInCartRequestBody(
    @SerialName("barcode")
    val barcode: String,

    @SerialName("quantity")
    val count: Int,
)
