package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateOrderPodeliPaymentStatusRequestBody(
    @SerialName("order_id")
    val orderId: String,
)
