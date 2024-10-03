package ru.livetyping.zarina.presentation.screen.cart.stateholder

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.util.compose.text.clear
import ru.livetyping.zarina.util.compose.text.textAsFlow

class CartPromoCodeStateHolder(savedStateHandle: SavedStateHandle) {
    private val _isPromoCodeInvalid = MutableStateFlow(false)
    val isPromoCodeInvalid: StateFlow<Boolean> = _isPromoCodeInvalid.asStateFlow()

    private val _promoCodeDescription = MutableStateFlow<Text?>(null)
    val promoCodeDescription: StateFlow<Text?> = _promoCodeDescription.asStateFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    val promoCodeTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val promoCode: String
        get() = promoCodeTextFieldState.text.toString()

    val promoCodeAsFlow: Flow<CharSequence>
        get() = promoCodeTextFieldState.textAsFlow()

    fun setIsPromoCodeInvalid(isInvalid: Boolean) {
        _isPromoCodeInvalid.value = isInvalid
    }

    fun setPromoCodeDescription(description: Text?) {
        _promoCodeDescription.value = description
    }

    fun clearPromoCode() {
        promoCodeTextFieldState.clearText()
    }

    fun updateFromCart(cart: Cart) {
        promoCodeTextFieldState.edit {
            clear()
            if (cart.promoCode != null) {
                append(cart.promoCode.value)
                placeCursorAtEnd()
            }
        }
    }
}
