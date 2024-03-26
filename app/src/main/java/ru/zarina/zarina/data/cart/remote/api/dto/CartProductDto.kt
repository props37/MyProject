package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.common.remote.api.dto.PriceDto
import ru.zarina.zarina.domain.cart.CartProduct
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductColor
import ru.zarina.zarina.domain.product.ProductOffer
import ru.zarina.zarina.domain.common.Color as DomainColor

@Serializable
data class CartProductDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("offer")
    val offer: Offer? = null,

    @SerialName("quantity")
    val count: Int? = null,
) {
    fun toCartProduct(): CartProduct {
        checkNotNull(id) { "id is null" }
        checkNotNull(offer) { "offer is null" }
        checkNotNull(offer.productId) { "id is null" }
        checkNotNull(offer.id) { "id is null" }
        checkNotNull(offer.name) { "name is null" }
        checkNotNull(offer.price) { "price is null" }
        checkNotNull(offer.barcode) { "barcode is null" }
        checkNotNull(offer.color) { "color is null" }
        checkNotNull(offer.imageUrl) { "imageUrl is null" }
        checkNotNull(offer.size) { "size is null" }
        checkNotNull(count) { "count is null" }
        return CartProduct(
            id = CartProduct.Id(id),
            productId = Product.Id(offer.productId),
            offerId = ProductOffer.Id(offer.id),
            name = offer.name,
            price = offer.price.toPrice(),
            barcode = Barcode(offer.barcode),
            color = offer.color.toProductColor(),
            imageUrl = Url(offer.imageUrl),
            size = offer.size,
            height = offer.height?.takeIf { it.isNotBlank() },
            count = count,
            isInFavorites = offer.isInFavorites ?: false,
            availableCount = CartProduct.AvailableCount(
                delivery = offer.deliveryAvailableCount ?: 0,
                pickUpFromShop = offer.pickUpFromShopAvailableCount ?: 0,
            ),
        )
    }

    @Serializable
    data class Offer(
        @SerialName("id")
        val id: String? = null,

        @SerialName("barcode")
        val barcode: String? = null,

        @SerialName("group_id")
        val productId: String? = null,

        @SerialName("title")
        val name: String? = null,

        @SerialName("color")
        val color: Color? = null,

        @SerialName("cover_picture")
        val imageUrl: String? = null,

        @SerialName("size_name")
        val size: String? = null,

        @SerialName("growth")
        val height: String? = null,

        @SerialName("price")
        val price: PriceDto? = null,

        @SerialName("is_favorite")
        val isInFavorites: Boolean? = null,

        @SerialName("online_amount")
        val deliveryAvailableCount: Int? = null,

        @SerialName("retail_amount")
        val pickUpFromShopAvailableCount: Int? = null,
    ) {
        @Serializable
        data class Color(
            @SerialName("id") 
            val id: String? = null,
            
            @SerialName("title") 
            val name: String? = null,
            
            @SerialName("code")
            val code: String? = null,

            @SerialName("product_id")
            val productId: String? = null,
        ) {
            fun toProductColor(): ProductColor {
                checkNotNull(id) { "id is null" }
                checkNotNull(name) { "name is null" }
                checkNotNull(code) { "code is null" }
                checkNotNull(productId) { "productId is null" }
                return ProductColor(
                    id = ProductColor.Id(id),
                    name = name,
                    color = DomainColor(code),
                    productId = Product.Id(productId),
                )
            }
        }
    }
}
