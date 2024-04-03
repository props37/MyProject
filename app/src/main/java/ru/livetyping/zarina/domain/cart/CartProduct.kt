package ru.livetyping.zarina.domain.cart

import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.ProductOffer

data class CartProduct(
    val id: Id,
    val productId: Product.Id,
    val offerId: ProductOffer.Id,
    val name: String,
    val price: Price,
    val barcode: Barcode,
    val color: ProductColor,
    val imageUrl: Url,
    val size: String,
    val height: String?,
    val count: Int,
    val isInFavorites: Boolean,
    val availableCount: AvailableCount,
) {
    @JvmInline
    value class Id(val value: Long)

    data class AvailableCount(
        val delivery: Int,
        val pickUpFromShop: Int,
    )
}

fun CartProduct.getAvailableCountForDeliveryType(deliveryType: DeliveryType): Int {
    return when (deliveryType) {
        DeliveryType.DELIVERY -> availableCount.delivery
        DeliveryType.PICK_UP_FROM_SHOP -> availableCount.pickUpFromShop
    }
}
