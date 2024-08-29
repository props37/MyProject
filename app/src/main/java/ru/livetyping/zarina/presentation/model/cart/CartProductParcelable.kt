package ru.livetyping.zarina.presentation.model.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.presentation.model.product.PriceParcelable
import ru.livetyping.zarina.presentation.model.product.ProductColorParcelable

@Serializable
@Parcelize
data class CartProductParcelable(
    val id: Long,
    val productId: String,
    val offerId: String,
    val name: String,
    val price: PriceParcelable,
    val barcode: String,
    val color: ProductColorParcelable,
    val imageUrl: String,
    val size: String,
    val height: String?,
    val count: Int,
    val isInFavorites: Boolean,
    val availableCount: AvailableCount,
) : Parcelable {

    fun toCartProduct(): CartProduct {
        return CartProduct(
            id = CartProduct.Id(id),
            productId = Product.Id(productId),
            offerId = ProductOffer.Id(offerId),
            name = name,
            price = price.toPrice(),
            barcode = Barcode(barcode),
            color = color.toProductColor(),
            imageUrl = Url(imageUrl),
            size = size,
            height = height,
            count = count,
            isInFavorites = isInFavorites,
            availableCount = CartProduct.AvailableCount(
                delivery = availableCount.delivery,
                pickup = availableCount.pickup,
            ),
        )
    }

    @Serializable
    @Parcelize
    data class AvailableCount(
        val delivery: Int,
        val pickup: Int,
    ) : Parcelable

    companion object {
        fun from(product: CartProduct): CartProductParcelable {
            return CartProductParcelable(
                id = product.id.value,
                productId = product.productId.value,
                offerId = product.offerId.value,
                name = product.name,
                price = PriceParcelable.from(product.price),
                barcode = product.barcode.value,
                color = ProductColorParcelable.from(product.color),
                imageUrl = product.imageUrl.value,
                size = product.size,
                height = product.height,
                count = product.count,
                isInFavorites = product.isInFavorites,
                availableCount = AvailableCount(
                    delivery = product.availableCount.delivery,
                    pickup = product.availableCount.pickup,
                ),
            )
        }
    }
}
