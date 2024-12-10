package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

@Serializable
@Parcelize
public data class ProductOfferParcelable(
    val id: String,
    val size: String,
    val sizeRu: String?,
    val isAvailable: Boolean,
    val height: String?,
    val barcode: String,
    val onlineCount: Int,
    val retailCount: Int,
) : Parcelable {
    public fun toProductOffer(): ProductOffer {
        return ProductOffer(
            id = ProductOffer.Id(id),
            size = size,
            sizeRu = sizeRu,
            isAvailable = isAvailable,
            height = height,
            barcode = Barcode(barcode),
            onlineCount = onlineCount,
            retailCount = retailCount,
        )
    }

    public companion object {
        public fun from(productOffer: ProductOffer): ProductOfferParcelable {
            return ProductOfferParcelable(
                id = productOffer.id.value,
                size = productOffer.size,
                sizeRu = productOffer.sizeRu,
                isAvailable = productOffer.isAvailable,
                height = productOffer.height,
                barcode = productOffer.barcode.value,
                onlineCount = productOffer.onlineCount,
                retailCount = productOffer.retailCount,
            )
        }
    }
}