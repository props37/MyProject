package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.cart.remote.api.dto.CartTypeDto
import ru.livetyping.zarina.data.order.remote.api.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.domain.order.PaymentMethodType

@Serializable
data class ApplyGiftCertificateRequestBody(
    @SerialName("barcode")
    val certificateNumber: String,

    @SerialName("verification_code")
    val certificateVerificationCode: String,

    @SerialName("order_price")
    val cartFinalPrice: Int,

    @SerialName("cart_type")
    val cartType: CartTypeDto,

    @SerialName("payment_method")
    val paymentMethodType: PaymentMethodTypeDto = PaymentMethodTypeDto.from(PaymentMethodType.GIFT_CARD),
)
