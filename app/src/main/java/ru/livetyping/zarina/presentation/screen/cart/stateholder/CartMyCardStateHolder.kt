package ru.livetyping.zarina.presentation.screen.cart.stateholder

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartMyCardStateHolder {
    private val _isMyCardApplied = MutableStateFlow(false)
    val isMyCardApplied: StateFlow<Boolean> = _isMyCardApplied.asStateFlow()

    fun setIsMyCardApplied(isApplied: Boolean) {
        _isMyCardApplied.value = isApplied
    }
}
