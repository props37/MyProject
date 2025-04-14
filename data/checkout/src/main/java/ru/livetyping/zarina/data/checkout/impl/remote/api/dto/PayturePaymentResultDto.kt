package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PayturePaymentResultDto(
    @SerialName("success")
    val success: Boolean? = null,
)
