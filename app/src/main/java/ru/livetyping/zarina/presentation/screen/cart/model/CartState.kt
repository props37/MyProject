package ru.livetyping.zarina.presentation.screen.cart.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.domain.cart.CartPrice
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.domain.cart.Cart as DomainCart

@Stable
sealed class CartState {
    data object Loading : CartState()

    @Immutable
    data class Cart(
        val productItems: ImmutableList<CartProductItem>,
        val price: CartPrice,
        val bonusState: CartBonusState,
        val myCardState: CartMyCardState?,
        val promoCodeState: CartPromoCodeState?,
        val productLimit: DomainCart.ProductLimit,
    ) : CartState()

    data object EmptyCart : CartState()

    @Immutable
    data class Error(val state: ErrorState) : CartState()
}
