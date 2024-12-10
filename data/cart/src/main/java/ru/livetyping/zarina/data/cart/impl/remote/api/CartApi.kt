package ru.livetyping.zarina.data.cart.impl.remote.api

import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductIdsDto

internal interface CartApi {
    suspend fun getCartProductIds(): CartProductIdsDto
}
