package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.cart.Cart

@Stable
internal data class CartBonusAccountState(
    val bonusAccount: Cart.BonusAccount,
    val isRedemptionAvailable: Boolean,
    val isRedemptionApplied: Boolean,
    val redemptionTextFieldState: TextFieldState,
)
