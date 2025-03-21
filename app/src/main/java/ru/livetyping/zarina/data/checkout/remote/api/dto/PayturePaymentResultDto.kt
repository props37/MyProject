package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayturePaymentResultDto(
    @SerialName("success")
    val success: Boolean? = null,
)
