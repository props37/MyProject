package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector

import androidx.compose.runtime.Immutable

@Immutable
internal data class ProductCountItem(
    val count: Int,
    val isSelected: Boolean,
    val isLoading: Boolean,
)
