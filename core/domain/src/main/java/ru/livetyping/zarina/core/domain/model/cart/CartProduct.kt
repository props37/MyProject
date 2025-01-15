package ru.livetyping.zarina.core.domain.model.cart

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductPrice

// Marked as stable on config/compose/stability_config.txt
public data class CartProduct(
    val id: Id,
    val productId: Product.Id,
    val offerId: ProductOffer.Id,
    val name: String,
    val price: ProductPrice,
    val barcode: Barcode,
    val color: ProductColor,
    val imageUrl: Url,
    val size: String,
    val height: String?,
    val count: Int,
    val isInWishlist: Boolean,
    val availableCount: AvailableCount,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    public data class AvailableCount(
        val delivery: Int,
        val pickup: Int,
    )
}

public fun CartProduct.getAvailableCountForCartType(cartType: CartType): Int {
    return when (cartType) {
        CartType.DELIVERY -> availableCount.delivery
        CartType.PICKUP -> availableCount.pickup
    }
}
