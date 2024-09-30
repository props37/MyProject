package ru.livetyping.zarina.presentation.screen.cart.stateholder

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartBonusStateHolder(savedStateHandle: SavedStateHandle) {
    private val _isBonusWriteOffApplied = MutableStateFlow(false)
    val isBonusWriteOffApplied: StateFlow<Boolean> = _isBonusWriteOffApplied.asStateFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    val bonusWriteOffTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    fun setIsBonusWriteOffApplied(isApplied: Boolean) {
        _isBonusWriteOffApplied.value = isApplied
    }
}
