package ru.zarina.zarina.ui.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.domain.rework.product.ProductOffer

@Serializable
@Parcelize
data class ProductOfferParcelable(
    val id: String,
    val size: String,
    val sizeRu: String?,
    val isAvailable: Boolean,
    val height: String?,
    val barcode: String,
    val onlineCount: Int,
    val retailCount: Int,
) : Parcelable {
    fun toProductOffer(): ProductOffer = ProductOffer(
        id = ProductOffer.Id(id),
        size = size,
        sizeRu = sizeRu,
        isAvailable = isAvailable,
        height = height,
        barcode = Barcode(barcode),
        onlineCount = onlineCount,
        retailCount = retailCount,
    )

    companion object {
        fun from(productOffer: ProductOffer): ProductOfferParcelable = ProductOfferParcelable(
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
