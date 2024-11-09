package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.order.remote.api.dto.PaymentMethodTypeDto

@Serializable
data class RemoveGiftCertificateRequestBody(
    @SerialName("payment_method")
    val paymentMethodType: PaymentMethodTypeDto,
)
