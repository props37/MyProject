package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.cart.getAvailableCountForCartType
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class CartStateBuilder {
    fun build(
        cartResult: Result<Cart>?,
        cartLoadingState: FlowRequester.LoadingState,
        cartType: CartType,
        isBonusRedemptionApplied: Boolean,
        bonusRedemptionTextFieldState: TextFieldState,
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
                    createCartStateFromCart(
                        cart = cart,
                        cartType = cartType,
                        isBonusRedemptionApplied = isBonusRedemptionApplied,
                        bonusRedemptionTextFieldState = bonusRedemptionTextFieldState,
                        isMyCardApplied = isMyCardApplied,
                        promoCodeTextFieldState = promoCodeTextFieldState,
                        isPromoCodeInvalid = isPromoCodeInvalid,
                        promoCodeDescription = promoCodeDescription,
                    )
                },
                onFailure = { throwable ->
                    val errorState = ZarinaErrorScreenState.from(throwable)
                    CartState.Error(errorState)
                },
            )
        }
    }

    private fun createCartStateFromCart(
        cart: Cart,
        cartType: CartType,
        isBonusRedemptionApplied: Boolean,
        bonusRedemptionTextFieldState: TextFieldState,
        isMyCardApplied: Boolean,
        promoCodeTextFieldState: TextFieldState,
        isPromoCodeInvalid: Boolean,
        promoCodeDescription: Text?,
    ): CartState {
        return if (cart.products.isNotEmpty()) {
            val productItems = createProductItems(cart, cartType).toImmutableList()
            val isBonusWriteOffAvailable =
                cart.bonusAccount.balance > 0 && cart.myCard?.isApplied != true
            val bonusState = CartBonusAccountState(
                bonusAccount = cart.bonusAccount,
                isRedemptionAvailable = isBonusWriteOffAvailable && cart.promoCode?.isApplied != true,
                isRedemptionApplied = isBonusRedemptionApplied || cart.bonusAccount.redemption.isApplied,
                redemptionTextFieldState = bonusRedemptionTextFieldState,
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
                bonusAccountState = bonusState,
                myCardState = myCardState,
                promoCodeState = promoCodeState,
                productLimit = cart.productLimit,
            )
        } else {
            CartState.EmptyCart
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
