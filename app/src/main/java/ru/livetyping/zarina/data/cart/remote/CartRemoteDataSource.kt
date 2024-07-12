package ru.livetyping.zarina.data.cart.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.cart.remote.api.CartApi
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartProductCount
import ru.livetyping.zarina.domain.cart.CartProductIds
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: CartApi,
) {
    fun getCartProductIdsFlow(): Flow<CartProductIds> = flow {
        val cartProductIds = api.getCartProductIds().toCartProductIds()
        emit(cartProductIds)
    }

    fun getCartFlow(cartType: CartType, cityKladrId: KladrId?): Flow<Cart> = flow {
        val cart = api.getCart(cartType, cityKladrId).toCart(cartType)
        emit(cart)
    }

    suspend fun applyMyCardToCart(cartType: CartType, productsFirstPriceSum: Int) {
        api.applyMyCardToCart(cartType, productsFirstPriceSum)
    }

    suspend fun removeMyCardFromCart(cartType: CartType) {
        api.removeMyCardFromCart(cartType)
    }

    suspend fun applyPromoCode(promoCode: String) {
        api.applyPromoCode(promoCode)
    }

    suspend fun removePromoCode() {
        api.removePromoCode()
    }

    suspend fun applyBonusWriteOff(cartType: CartType, bonusCount: Int) {
        api.applyBonusWriteOff(cartType, bonusCount)
    }

    suspend fun removeBonusWriteOff(cartType: CartType) {
        api.removeBonusWriteOff(cartType)
    }

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCount {
        return api.addProductToCard(barcode, count).toCartProductCount()
    }

    suspend fun removeProductFromCart(barcode: Barcode): CartProductCount {
        return api.removeProductFromCart(barcode).toCartProductCount()
    }

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int, cartType: CartType) {
        api.changeProductCountInCart(barcode, count, cartType)
    }

    suspend fun clearCart() {
        api.clearCart()
    }
}
