package ru.zarina.zarina.domain.cart

import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.domain.product.Price
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductColor
import ru.zarina.zarina.domain.product.ProductOffer

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
) {
    @JvmInline
    value class Id(val value: Long)
}
