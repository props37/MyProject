package ru.livetyping.zarina.ui.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.ui.model.common.MediaParcelable

@Serializable
@Parcelize
data class ProductItemParcelable(
    val id: String,
    val name: String,
    val price: PriceParcelable,
    val offers: List<ProductOfferParcelable>,
    val colors: List<ProductColorParcelable>,
    val media: List<MediaParcelable>,
    val isInFavorites: Boolean,
    val isInCart: Boolean,
) : Parcelable {
    fun toProductItem(): ProductItem = ProductItem(
        id = Product.Id(id),
        name = name,
        price = price.toPrice(),
        offers = offers.map { it.toProductOffer() },
        colors = colors.map { it.toProductColor() },
        media = media.map { it.toMedia() },
        isInFavorites = isInFavorites,
        isInCart = isInCart,
    )

    companion object {
        fun from(product: Product): ProductItemParcelable = ProductItemParcelable(
            id = product.id.value,
            name = product.name,
            price = PriceParcelable.from(product.price),
            offers = product.offers.map { ProductOfferParcelable.from(it) },
            colors = product.colors.map { ProductColorParcelable.from(it) },
            media = product.media.map { MediaParcelable.from(it) },
            isInFavorites = product.isInFavorites,
            isInCart = product.isInCart,
        )
    }
}
