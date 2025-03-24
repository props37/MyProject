package ru.livetyping.zarina.feature.cart.ui.impl.impl.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class CheckoutTopBarState(
    val checkoutStep: Int,
    val checkoutStepCount: Int,
)
