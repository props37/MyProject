package ru.livetyping.zarina.data.cart.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.cart.remote.api.CartApi
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartProductCount
import ru.livetyping.zarina.domain.cart.CartProductIds
import ru.livetyping.zarina.domain.cart.DeliveryType
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

    fun getCartFlow(deliveryType: DeliveryType, cityKladrId: KladrId?): Flow<Cart> = flow {
        val cart = api.getCart(deliveryType, cityKladrId).toCart(deliveryType)
        emit(cart)
    }

    suspend fun applyMyCardToCart(deliveryType: DeliveryType, productsFirstPriceSum: Int) {
        api.applyMyCardToCart(deliveryType, productsFirstPriceSum)
    }

    suspend fun removeMyCardFromCart(deliveryType: DeliveryType) {
        api.removeMyCardFromCart(deliveryType)
    }

    suspend fun applyPromoCode(promoCode: String) {
        api.applyPromoCode(promoCode)
    }

    suspend fun removePromoCode() {
        api.removePromoCode()
    }

    suspend fun applyBonusWriteOff(deliveryType: DeliveryType, bonusCount: Int) {
        api.applyBonusWriteOff(deliveryType, bonusCount)
    }

    suspend fun removeBonusWriteOff(deliveryType: DeliveryType) {
        api.removeBonusWriteOff(deliveryType)
    }

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCount {
        return api.addProductToCard(barcode, count).toCartProductCount()
    }

    suspend fun removeProductFromCart(barcode: Barcode): CartProductCount {
        return api.removeProductFromCart(barcode).toCartProductCount()
    }

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int, deliveryType: DeliveryType) {
        api.changeProductCountInCart(barcode, count, deliveryType)
    }

    suspend fun clearCart() {
        api.clearCart()
    }
}
