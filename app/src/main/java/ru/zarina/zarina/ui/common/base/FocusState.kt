package ru.zarina.zarina.ui.common.base

data class FocusState(
    val isFocused: Boolean = false,
    val everLostFocus: Boolean = false,
) {
    fun updated(isFocused: Boolean) =
        FocusState(isFocused, (this.isFocused && !isFocused) || everLostFocus)
}
