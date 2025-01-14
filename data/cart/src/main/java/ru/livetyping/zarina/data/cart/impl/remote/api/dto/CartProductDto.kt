package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.ProductPriceDto
import timber.log.Timber
import ru.livetyping.zarina.core.domain.model.product.ProductColor as ProductColorDomain

@Serializable
internal data class CartProductDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("offer")
    val offer: Offer? = null,

    @SerialName("quantity")
    val quantity: Int? = null,
) {
    fun toCartProduct(): CartProduct {
        checkPropertyNotNull(id) { ::id }
        checkPropertyNotNull(offer) { ::offer }
        val color = offer.color?.toProductColor()
        checkPropertyNotNull(offer.groupId) { offer::groupId }
        checkPropertyNotNull(offer.id) { offer::id }
        checkPropertyNotNull(offer.title) { offer::title }
        checkPropertyNotNull(offer.price) { offer::price }
        checkPropertyNotNull(offer.barcode) { offer::barcode }
        checkPropertyNotNull(offer.color) { offer::color }
        checkPropertyNotNull(offer.coverPicture) { offer::coverPicture }
        checkPropertyNotNull(offer.sizeName) { offer::sizeName }
        checkPropertyNotNull(quantity) { ::quantity }
        checkNotNull(color) { "product color is null" }
        return CartProduct(
            id = CartProduct.Id(id.toString()),
            productId = Product.Id(offer.groupId),
            offerId = ProductOffer.Id(offer.id),
            name = offer.title,
            price = offer.price.toProductPrice(),
            barcode = Barcode(offer.barcode),
            color = color,
            imageUrl = Url.create(offer.coverPicture),
            size = offer.sizeName,
            height = offer.growth?.takeIf { it.isNotBlank() },
            count = quantity,
            isInFavorites = offer.isFavorite ?: false,
            availableCount = CartProduct.AvailableCount(
                delivery = offer.onlineAmount ?: 0,
                pickup = offer.retailAmount ?: 0,
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
        val groupId: String? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("color")
        val color: ProductColor? = null,

        @SerialName("cover_picture")
        val coverPicture: String? = null,

        @SerialName("size_name")
        val sizeName: String? = null,

        @SerialName("growth")
        val growth: String? = null,

        @SerialName("price")
        val price: ProductPriceDto? = null,

        @SerialName("is_favorite")
        val isFavorite: Boolean? = null,

        @SerialName("online_amount")
        val onlineAmount: Int? = null,

        @SerialName("retail_amount")
        val retailAmount: Int? = null,
    ) {
        @Serializable
        data class ProductColor(
            @SerialName("id")
            val id: String? = null,

            @SerialName("title")
            val title: String? = null,

            @SerialName("code")
            val code: String? = null,

            @SerialName("product_id")
            val productId: String? = null,
        ) {
            fun toProductColor(): ProductColorDomain? {
                return if (id != null && title != null && code != null && productId != null) {
                    ProductColorDomain(
                        id = ProductColorDomain.Id(id),
                        name = title,
                        color = Color(code),
                        productId = Product.Id(productId),
                    )
                } else {
                    Timber.tag(TAG).e("Drop ProductColor because its ID, name, color code or product ID is null")
                    null
                }
            }
        }
    }

    private companion object {
        private const val TAG = "CartProductDto"
    }
}
