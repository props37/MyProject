package ru.livetyping.zarina.data.cart.impl.remote.api

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartDto
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductCountDto
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductIdsDto

internal interface CartApi {
    suspend fun getCart(cartType: CartType, cityKladrId: KladrId?): CartDto

    suspend fun getCartProductIds(): CartProductIdsDto

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCountDto

    suspend fun remoteProductFromCart(barcode: Barcode): CartProductCountDto

    suspend fun applyMyCard(cartType: CartType, productsFirstPriceSum: Int)

    suspend fun withdrawMyCard(cartType: CartType)

    suspend fun applyPromoCode(promoCode: String)

    suspend fun withdrawPromoCode()

    suspend fun redeemBonuses(cartType: CartType, bonusCount: Int)

    suspend fun cancelBonusRedemption(cartType: CartType)

    suspend fun clearCart()
}
