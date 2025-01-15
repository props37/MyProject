package ru.livetyping.zarina.data.cart.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.data.cart.impl.model.CartProductCount
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds

internal interface CartRemoteDataSource {
    fun getCartFlow(cartType: CartType, cityKladrId: KladrId?): Flow<Cart>

    fun getCartProductIdsFlow(): Flow<CartProductIds>

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCount

    suspend fun removeProductFromCart(barcode: Barcode): CartProductCount

    suspend fun changeProductCount(barcode: Barcode, count: Int, cartType: CartType)

    suspend fun applyMyCard(cartType: CartType, productsFirstPriceSum: Int)

    suspend fun withdrawMyCard(cartType: CartType)

    suspend fun applyPromoCode(promoCode: String)

    suspend fun withdrawPromoCode()

    suspend fun redeemBonuses(cartType: CartType, bonusCount: Int)

    suspend fun cancelBonusRedemption(cartType: CartType)

    suspend fun clearCart()
}
