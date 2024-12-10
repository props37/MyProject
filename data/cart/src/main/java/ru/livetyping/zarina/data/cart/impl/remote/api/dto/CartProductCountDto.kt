package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.data.cart.impl.model.CartProductCount

@Serializable
internal data class CartProductCountDto(
    @SerialName("total_count")
    val totalCount: Int? = null,
) {
    fun toCartProductCount(): CartProductCount {
        checkPropertyNotNull(totalCount) { ::totalCount }
        return CartProductCount(totalCount)
    }
}
