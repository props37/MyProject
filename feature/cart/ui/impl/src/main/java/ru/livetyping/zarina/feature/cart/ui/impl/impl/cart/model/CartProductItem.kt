package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.cart.CartProduct

@Immutable
internal data class CartProductItem(
    val product: CartProduct,
    val availableCount: Int,
)
