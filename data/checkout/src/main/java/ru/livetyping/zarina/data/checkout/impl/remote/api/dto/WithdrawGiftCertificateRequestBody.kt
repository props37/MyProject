package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.network.zarina.dto.PaymentMethodTypeDto

@Serializable
internal data class WithdrawGiftCertificateRequestBody(
    @SerialName("payment_method")
    val paymentMethodType: PaymentMethodTypeDto,
)
