package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartPrice
import ru.livetyping.zarina.domain.cart.CartSize

@Serializable
data class CartDto(
    @SerialName("items")
    val products: List<CartProductDto>? = null,

    @SerialName("total_count")
    val totalProductCount: Int? = null,

    @SerialName("delivery_count")
    val deliveryProductCount: Int? = null,

    @SerialName("retail_count")
    val pickUpFromStoresProductCount: Int? = null,

    @SerialName("total_sum")
    val totalPrice: Int? = null,

    @SerialName("total_discount")
    val discountSize: Int? = null,

    @SerialName("discount")
    val cartPrice: Int? = null,
) {
    fun toCart(): Cart {
        checkNotNull(products) { "products is null" }
        return Cart(
            products = products.map { it.toCartProduct() },
            size = getCartSize(),
            price = getCartPrice(),
        )
    }

    private fun getCartSize(): CartSize {
        checkNotNull(totalProductCount) { "totalProductCount is null" }
        checkNotNull(deliveryProductCount) { "deliveryProductCount is null" }
        checkNotNull(pickUpFromStoresProductCount) { "pickUpFromStoresProductCount is null" }
        return CartSize(
            totalProductCount = totalProductCount,
            deliveryProductCount = deliveryProductCount,
            pickUpFromStoreProductCount = pickUpFromStoresProductCount,
        )
    }

    private fun getCartPrice(): CartPrice {
        checkNotNull(totalPrice) { "cartPrice is null" }
        checkNotNull(discountSize) { "discountSize is null" }
        checkNotNull(cartPrice) { "totalPrice is null" }
        return CartPrice(
            cartPrice = cartPrice,
            discountSize = discountSize,
            totalPrice = totalPrice,
        )
    }
}
