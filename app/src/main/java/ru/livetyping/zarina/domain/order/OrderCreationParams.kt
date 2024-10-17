package ru.livetyping.zarina.domain.order

import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.PaymentData

data class OrderCreationParams(
    val cart: Cart,
    val paymentMethodType: PaymentMethodType,
    val checkoutParams: CheckoutParams,
    val paymentData: PaymentData?,
)
