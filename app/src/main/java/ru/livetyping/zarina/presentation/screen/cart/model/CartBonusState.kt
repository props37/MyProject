package ru.livetyping.zarina.presentation.screen.cart.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.domain.cart.Cart

@Stable
data class CartBonusState(
    val bonuses: Cart.Bonuses,
    val isWriteOffAvailable: Boolean,
    val isWriteOffApplied: Boolean,
    val writeOffTextFieldState: TextFieldState,
)
