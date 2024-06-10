package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PriceDto
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import timber.log.Timber
import ru.livetyping.zarina.domain.product.ProductColor as DomainProductColor

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
        val color = offer.color?.toProductColor()
        checkNotNull(offer.productId) { "id is null" }
        checkNotNull(offer.id) { "id is null" }
        checkNotNull(offer.name) { "name is null" }
        checkNotNull(offer.price) { "price is null" }
        checkNotNull(offer.barcode) { "barcode is null" }
        checkNotNull(offer.color) { "color is null" }
        checkNotNull(offer.imageUrl) { "imageUrl is null" }
        checkNotNull(offer.size) { "size is null" }
        checkNotNull(count) { "count is null" }
        checkNotNull(color) { "color is null" }
        return CartProduct(
            id = CartProduct.Id(id),
            productId = Product.Id(offer.productId),
            offerId = ProductOffer.Id(offer.id),
            name = offer.name,
            price = offer.price.toPrice(),
            barcode = Barcode(offer.barcode),
            color = color,
            imageUrl = Url(offer.imageUrl),
            size = offer.size,
            height = offer.height?.takeIf { it.isNotBlank() },
            count = count,
            isInFavorites = offer.isInFavorites ?: false,
            availableCount = CartProduct.AvailableCount(
                delivery = offer.deliveryAvailableCount ?: 0,
                pickUpFromStore = offer.pickUpFromStoreAvailableCount ?: 0,
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
        val color: ProductColor? = null,

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
        val pickUpFromStoreAvailableCount: Int? = null,
    ) {
        @Serializable
        data class ProductColor(
            @SerialName("id")
            val id: String? = null,

            @SerialName("title")
            val name: String? = null,

            @SerialName("code")
            val code: String? = null,

            @SerialName("product_id")
            val productId: String? = null,
        ) {
            fun toProductColor(): DomainProductColor? {
                return if (id != null && name != null && code != null && productId != null) {
                    DomainProductColor(
                        id = DomainProductColor.Id(id),
                        name = name,
                        color = Color(code),
                        productId = Product.Id(productId),
                    )
                } else {
                    Timber.e("Drop ProductColor because its ID, name, color code or product ID is null")
                    null
                }
            }
        }
    }
}
