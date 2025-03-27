package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class ToggleableFilter(
    val filter: Filter,
    val isApplied: Boolean,
)
