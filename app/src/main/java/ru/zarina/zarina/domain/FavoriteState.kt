package ru.zarina.zarina.domain

data class FavoriteState(
    val isFavorite: Boolean,
    /** Whether the current state is a result of state reset due to update error */
    val isErrorReset: Boolean = false,
)