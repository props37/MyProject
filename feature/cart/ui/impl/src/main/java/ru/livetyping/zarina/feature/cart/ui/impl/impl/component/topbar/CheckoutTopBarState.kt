package ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar

import androidx.compose.runtime.Immutable

@Immutable
internal data class CheckoutTopBarState(
    val checkoutStep: Int,
    val checkoutStepCount: Int,
    val isBackButtonVisible: Boolean,
)
