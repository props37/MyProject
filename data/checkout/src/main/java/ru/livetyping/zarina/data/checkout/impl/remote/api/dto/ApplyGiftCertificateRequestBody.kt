package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.network.zarina.dto.CartTypeDto
import ru.livetyping.zarina.core.network.zarina.dto.PaymentMethodTypeDto

@Serializable
internal data class ApplyGiftCertificateRequestBody(
    @SerialName("barcode")
    val certificateNumber: String,

    @SerialName("verification_code")
    val certificateVerificationCode: String,

    @SerialName("order_price")
    val cartFinalPrice: Int,

    @SerialName("cart_type")
    val cartType: CartTypeDto,

    @SerialName("payment_method")
    val paymentMethodType: PaymentMethodTypeDto = PaymentMethodTypeDto.from(PaymentMethodType.GIFT_CERTIFICATE),
)
