package ru.livetyping.zarina.presentation.screen.cart.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.presentation.base.text.Text

@Stable
data class CartPromoCodeState(
    val isApplied: Boolean,
    val isInvalid: Boolean,
    val description: Text?,
    val textFieldState: TextFieldState,
    val appliedPromoCode: String?,
)
