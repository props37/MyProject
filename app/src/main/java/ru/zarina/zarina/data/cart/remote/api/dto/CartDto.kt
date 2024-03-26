package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.cart.Cart
import ru.zarina.zarina.domain.cart.CartSize

@Serializable
data class CartDto(
    @SerialName("items")
    val products: List<CartProductDto>? = null,

    @SerialName("total_count")
    val totalProductCount: Int? = null,

    @SerialName("delivery_count")
    val deliveryProductCount: Int? = null,

    @SerialName("retail_count")
    val pickUpFromShopProductCount: Int? = null,
) {
    fun toCart(): Cart {
        checkNotNull(products) { "products is null" }
        return Cart(
            products = products.map { it.toCartProduct() },
            size = getCartSize(),
        )
    }

    private fun getCartSize(): CartSize {
        checkNotNull(totalProductCount) { "totalProductCount is null" }
        checkNotNull(deliveryProductCount) { "deliveryProductCount is null" }
        checkNotNull(pickUpFromShopProductCount) { "pickUpFromShopProductCount is null" }
        return CartSize(
            totalProductCount = totalProductCount,
            deliveryProductCount = deliveryProductCount,
            pickUpFromShopProductCount = pickUpFromShopProductCount,
        )
    }
}
