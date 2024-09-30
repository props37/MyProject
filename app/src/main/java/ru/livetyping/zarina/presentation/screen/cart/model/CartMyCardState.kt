package ru.livetyping.zarina.presentation.screen.cart.model

import androidx.compose.runtime.Immutable

@Immutable
data class CartMyCardState(
    val isApplied: Boolean,
    val info: String?,
)
