package ru.livetyping.zarina.presentation.screen.checkout.common

import ru.livetyping.zarina.domain.cart.CartType

val CartType.checkoutStepCount: Int
    get() = when (this) {
        CartType.DELIVERY -> CHECKOUT_STEP_COUNT_DELIVERY
        CartType.PICKUP -> CHECKOUT_STEP_COUNT_PICKUP
    }

private const val CHECKOUT_STEP_COUNT_DELIVERY = 4
private const val CHECKOUT_STEP_COUNT_PICKUP = 4
