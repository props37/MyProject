package ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.text.Text

@Immutable
internal data class CheckoutTopBarState(
    val title: Text,
    val checkoutStep: Int,
    val checkoutStepCount: Int,
    val isBackButtonVisible: Boolean,
)
