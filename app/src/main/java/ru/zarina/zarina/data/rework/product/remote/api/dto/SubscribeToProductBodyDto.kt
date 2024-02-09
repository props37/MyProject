package ru.zarina.zarina.data.rework.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeToProductBodyDto(
    @SerialName("barcodes")
    val barcodes: List<String>,

    @SerialName("email") 
    val email: String,

    @SerialName("first_name")
    val firstName: String,
)
