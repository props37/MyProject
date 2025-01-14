package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel

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
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicompose.clear
import ru.livetyping.zarina.core.uicompose.textAsFlow

internal class CartPromoCodeStateHolder(savedStateHandle: SavedStateHandle) {
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
            cart.promoCode?.let {
                append(it.value)
                placeCursorAtEnd()
            }
        }
    }
}
