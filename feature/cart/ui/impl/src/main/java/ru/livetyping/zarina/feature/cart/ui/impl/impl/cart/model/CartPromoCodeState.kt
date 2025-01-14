package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.text.Text

@Stable
internal data class CartPromoCodeState(
    val isApplied: Boolean,
    val isInvalid: Boolean,
    val description: Text?,
    val textFieldState: TextFieldState,
    val appliedPromoCode: String?,
)
