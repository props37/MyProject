package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.cart.CartPrice
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.domain.model.cart.Cart as CartDomain

@Stable
internal sealed class CartState {
    @Immutable
    data class Cart(
        val productItems: ImmutableList<CartProductItem>,
        val price: CartPrice,
        val bonusAccountState: CartBonusAccountState,
        val myCardState: CartMyCardState?,
        val promoCodeState: CartPromoCodeState?,
        val productLimit: CartDomain.ProductLimit,
    ) : CartState()

    data object EmptyCart : CartState()

    data object Loading : CartState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : CartState()
}
