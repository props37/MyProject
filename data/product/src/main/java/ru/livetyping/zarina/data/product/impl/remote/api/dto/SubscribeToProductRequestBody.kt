package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SubscribeToProductRequestBody(
    @SerialName("barcodes")
    val barcodes: List<String>,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("email") 
    val email: String,
)
