package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.cart.Cart

public data class OrderCreationParams(
    val cart: Cart,
    val paymentMethodType: PaymentMethodType,
    val checkoutParams: CheckoutParams,
    val paymentData: PaymentData?,
)
