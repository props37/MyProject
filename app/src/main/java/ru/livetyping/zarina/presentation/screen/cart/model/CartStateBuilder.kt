package ru.livetyping.zarina.presentation.screen.cart.model

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.cart.getAvailableCountForCartType
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.util.library.coroutines.FlowRequester

class CartStateBuilder {
    fun build(
        cartResult: Result<Cart>?,
        cartLoadingState: FlowRequester.LoadingState,
        cartType: CartType,
        isBonusWriteOffApplied: Boolean,
        bonusWriteOffTextFieldState: TextFieldState,
        isMyCardApplied: Boolean,
        promoCodeTextFieldState: TextFieldState,
        isPromoCodeInvalid: Boolean,
        promoCodeDescription: Text?,
    ): CartState {
        val isLoading = cartLoadingState.loadingRequest == CartRequest.LOADING
        val isPullRefreshing = cartLoadingState.loadingRequest == CartRequest.PULL_REFRESHING
        return if (cartResult == null || isLoading || isPullRefreshing) {
            CartState.Loading
        } else {
            cartResult.fold(
                onSuccess = { cart ->
                    if (cart.products.isNotEmpty()) {
                        val productItems =
                            createProductItems(cart, cartType).toImmutableList()
                        val isBonusWriteOffAvailable =
                            cart.bonuses.available > 0 && cart.myCard?.isApplied != true
                        val bonusState = CartBonusState(
                            bonuses = cart.bonuses,
                            isWriteOffAvailable = isBonusWriteOffAvailable && cart.promoCode?.isApplied != true,
                            isWriteOffApplied = isBonusWriteOffApplied || cart.bonuses.writeOff.isApplied,
                            writeOffTextFieldState = bonusWriteOffTextFieldState,
                        )
                        val myCardState = cart.myCard?.let {
                            CartMyCardState(
                                isApplied = isMyCardApplied,
                                info = it.info,
                            )
                        }
                        val promoCodeState = if (cart.myCard?.isApplied != true) {
                            CartPromoCodeState(
                                isApplied = cart.promoCode?.isApplied == true,
                                isInvalid = isPromoCodeInvalid,
                                description = promoCodeDescription,
                                textFieldState = promoCodeTextFieldState,
                                appliedPromoCode = cart.promoCode?.value,
                            )
                        } else null
                        CartState.Cart(
                            productItems = productItems,
                            price = cart.price,
                            bonusState = bonusState,
                            myCardState = myCardState,
                            promoCodeState = promoCodeState,
                            productLimit = cart.productLimit,
                        )
                    } else {
                        CartState.EmptyCart
                    }
                },
                onFailure = { throwable ->
                    val errorState = ErrorState.from(throwable)
                    CartState.Error(errorState)
                },
            )
        }
    }

    private fun createProductItems(
        cart: Cart,
        cartType: CartType,
    ): List<CartProductItem> {
        return cart.products
            .map { product ->
                val availableCount = product.getAvailableCountForCartType(cartType)
                CartProductItem(
                    product = product,
                    availableCount = availableCount,
                )
            }
    }
}
