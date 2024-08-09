package ru.livetyping.zarina.presentation.screen.orderplacement.common

import ru.livetyping.zarina.domain.cart.CartType

val CartType.orderPlacementStepCount: Int
    get() = when (this) {
        CartType.DELIVERY -> ORDER_PLACEMENT_STEP_COUNT_DELIVERY
        CartType.PICKUP -> ORDER_PLACEMENT_STEP_COUNT_PICKUP
    }

private const val ORDER_PLACEMENT_STEP_COUNT_DELIVERY = 4
private const val ORDER_PLACEMENT_STEP_COUNT_PICKUP = 3
