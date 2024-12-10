package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uimodel.common.MediaParcelable

@Serializable
@Parcelize
public data class ProductShortParcelable(
    val id: String,
    val name: String,
    val price: ProductPriceParcelable,
    val offers: List<ProductOfferParcelable>,
    val colors: List<ProductColorParcelable>,
    val media: List<MediaParcelable>,
    val isInWishlist: Boolean,
    val isInCart: Boolean,
) : Parcelable {
    public fun toProductShort(): ProductShort = ProductShort(
        id = Product.Id(id),
        name = name,
        price = price.toProductPrice(),
        offers = offers.map { it.toProductOffer() },
        colors = colors.map { it.toProductColor() },
        media = media.map { it.toMedia() },
        isInWishlist = isInWishlist,
        isInCart = isInCart,
    )

    public companion object {
        public fun from(product: Product): ProductShortParcelable = ProductShortParcelable(
            id = product.id.value,
            name = product.name,
            price = ProductPriceParcelable.from(product.price),
            offers = product.offers.map { ProductOfferParcelable.from(it) },
            colors = product.colors.map { ProductColorParcelable.from(it) },
            media = product.media.map { MediaParcelable.from(it) },
            isInWishlist = product.isInWishlist,
            isInCart = product.isInCart,
        )
    }
}
