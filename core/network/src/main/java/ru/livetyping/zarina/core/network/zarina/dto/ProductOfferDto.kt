package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import timber.log.Timber

@Serializable
public data class ProductOfferDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("size")
    val size: String? = null,

    @SerialName("size_ru")
    val sizeRu: String? = null,

    @SerialName("is_available")
    val isAvailable: Boolean? = null,

    @SerialName("growth")
    val growth: String? = null,

    @SerialName("barcode")
    val barcode: String? = null,

    @SerialName("online_quantity")
    val onlineQuantity: Int? = null,

    @SerialName("retail_quantity")
    val retailQuantity: Int? = null,
) {
    public fun toProductOffer(): ProductOffer? {
        return if (
            id != null
            && size != null
            && isAvailable != null
            && barcode != null
            && onlineQuantity != null
            && retailQuantity != null
        ) {
            ProductOffer(
                id = ProductOffer.Id(id),
                size = size,
                sizeRu = sizeRu,
                isAvailable = isAvailable,
                height = growth,
                barcode = Barcode(barcode),
                onlineCount = onlineQuantity,
                retailCount = retailQuantity,
            )
        } else {
            Timber.tag(TAG).e("Drop ProductOfferDto because its ID, size, isAvailable, barcode, onlineQuantity or retailQuantity is null")
            null
        }
    }

    private companion object {
        private const val TAG = "ProductOfferDto"
    }
}
