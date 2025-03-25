package ru.livetyping.zarina.core.uimodel.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.uimodel.product.ProductColorParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductPriceParcelable

@Parcelize
@Serializable
public data class CartProductParcelable(
    val id: String,
    val productId: String,
    val offerId: String,
    val name: String,
    val price: ProductPriceParcelable,
    val barcode: String,
    val color: ProductColorParcelable,
    val imageUrl: String,
    val size: String,
    val height: String?,
    val count: Int,
    val isInWishlist: Boolean,
    val availableCount: AvailableCountParcelable,
) : Parcelable {

    public fun toCartProduct(): CartProduct {
        return CartProduct(
            id = CartProduct.Id(id),
            productId = Product.Id(productId),
            offerId = ProductOffer.Id(offerId),
            name = name,
            price = price.toProductPrice(),
            barcode = Barcode(barcode),
            color = color.toProductColor(),
            imageUrl = Url.create(imageUrl),
            size = size,
            height = height,
            count = count,
            isInWishlist = isInWishlist,
            availableCount = CartProduct.AvailableCount(
                delivery = availableCount.delivery,
                pickup = availableCount.pickup,
            ),
        )
    }

    @Parcelize
    @Serializable
    public data class AvailableCountParcelable(
        val delivery: Int,
        val pickup: Int,
    ) : Parcelable

    public companion object {
        public fun from(product: CartProduct): CartProductParcelable {
            return CartProductParcelable(
                id = product.id.value,
                productId = product.productId.value,
                offerId = product.offerId.value,
                name = product.name,
                price = ProductPriceParcelable.from(product.price),
                barcode = product.barcode.value,
                color = ProductColorParcelable.from(product.color),
                imageUrl = product.imageUrl.value,
                size = product.size,
                height = product.height,
                count = product.count,
                isInWishlist = product.isInWishlist,
                availableCount = AvailableCountParcelable(
                    delivery = product.availableCount.delivery,
                    pickup = product.availableCount.pickup,
                ),
            )
        }
    }
}
