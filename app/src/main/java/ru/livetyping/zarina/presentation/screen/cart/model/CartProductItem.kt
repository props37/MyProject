package ru.livetyping.zarina.presentation.screen.cart.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.domain.cart.CartProduct

@Immutable
data class CartProductItem(
    val product: CartProduct,
    val availableCount: Int,
)
