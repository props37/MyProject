package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class CartMyCardState(
    val isApplied: Boolean,
    val info: String?,
)
