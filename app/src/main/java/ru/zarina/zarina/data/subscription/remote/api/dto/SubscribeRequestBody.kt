package ru.zarina.zarina.data.subscription.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRequestBody(
    @SerialName("barcode")
    val barcode: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("mail")
    val mail: String,
)
