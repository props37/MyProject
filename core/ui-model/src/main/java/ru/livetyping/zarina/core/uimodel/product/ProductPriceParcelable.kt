package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import java.math.BigDecimal

@Serializable
@Parcelize
public data class ProductPriceParcelable(
    val originalPrice: String,
    val discount: Discount?,
) : Parcelable {
    public fun toProductPrice(): ProductPrice {
        val discount = discount?.let {
            ProductPrice.Discount(
                discountPrice = BigDecimal(it.discountPrice),
                discountPercent = BigDecimal(it.discountPercent),
            )
        }
        return ProductPrice(
            originalPrice = BigDecimal(originalPrice),
            discount = discount,
        )
    }

    @Serializable
    @Parcelize
    public data class Discount(
        val discountPrice: String,
        val discountPercent: String,
    ) : Parcelable

    public companion object {
        public fun from(price: ProductPrice): ProductPriceParcelable {
            val discount = price.discount?.let {
                Discount(
                    discountPrice = it.discountPrice.toString(),
                    discountPercent = it.discountPercent.toString(),
                )
            }

            return ProductPriceParcelable(
                originalPrice = price.originalPrice.toString(),
                discount = discount,
            )
        }
    }
}
