package ru.livetyping.zarina.feature.cart.ui.impl.impl.util

import ru.livetyping.zarina.core.domain.model.cart.CartType

internal val CartType.checkoutStepCount: Int
    get() = when (this) {
        CartType.DELIVERY -> CHECKOUT_STEP_COUNT_DELIVERY
        CartType.PICKUP -> CHECKOUT_STEP_COUNT_PICKUP
    }

private const val CHECKOUT_STEP_COUNT_DELIVERY = 4
private const val CHECKOUT_STEP_COUNT_PICKUP = 4
