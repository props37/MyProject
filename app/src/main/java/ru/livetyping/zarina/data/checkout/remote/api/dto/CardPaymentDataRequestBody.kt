package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.product.currentPrice

@Serializable
data class CardPaymentDataRequestBody(
    @SerialName("products")
    val products: List<Product>? = null,

    @SerialName("amount")
    val finalPrice: Int,

    @SerialName("userId")
    val userId: String?,

    @SerialName("storeId") 
    val storeId: String?,
) {
    @Serializable
    data class Product(
        @SerialName("id")
        val id: String,

        @SerialName("barcode")
        val barcode: String,

        @SerialName("quantity")
        val count: Int,

        @SerialName("price")
        val price: Int,
    ) {
        companion object {
            fun from(product: CartProduct): Product {
                return Product(
                    id = product.offerId.value,
                    barcode = product.barcode.value,
                    count = product.count,
                    price = product.price.currentPrice,
                )
            }
        }
    }
}
