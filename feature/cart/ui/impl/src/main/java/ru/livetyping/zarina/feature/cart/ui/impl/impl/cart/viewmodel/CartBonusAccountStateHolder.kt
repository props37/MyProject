package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.uicompose.clear

internal class CartBonusAccountStateHolder(savedStateHandle: SavedStateHandle) {
    private val _isBonusRedemptionApplied = MutableStateFlow(false)
    val isBonusRedemptionApplied: StateFlow<Boolean> = _isBonusRedemptionApplied.asStateFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    val bonusRedemptionTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    fun setIsBonusRedemptionApplied(isApplied: Boolean) {
        _isBonusRedemptionApplied.value = isApplied
    }

    fun updateFromCart(cart: Cart) {
        setIsBonusRedemptionApplied(cart.bonusAccount.redemption.isApplied)
        bonusRedemptionTextFieldState.edit {
            clear()
            if (cart.bonusAccount.redemption.isApplied) {
                append(cart.bonusAccount.redemption.value.toString())
                placeCursorAtEnd()
            }
        }
    }
}
