package ru.livetyping.zarina.data.cart.impl.remote.api

import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductCountDto
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductIdsDto

internal interface CartApi {
    suspend fun getCartProductIds(): CartProductIdsDto

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCountDto

    suspend fun remoteProductFromCart(barcode: Barcode): CartProductCountDto

    suspend fun clearCart()
}
