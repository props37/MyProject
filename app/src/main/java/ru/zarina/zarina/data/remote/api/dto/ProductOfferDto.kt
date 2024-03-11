package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.domain.rework.product.ProductOffer
import timber.log.Timber

@Serializable
data class ProductOfferDto(
    @SerialName("id") 
    val id: String? = null,
    
    @SerialName("size") 
    val size: String? = null,
    
    @SerialName("size_ru")
    val sizeRu: String? = null,

    @SerialName("is_available")
    val isAvailable: Boolean? = null,

    @SerialName("growth")
    val height: String? = null,

    @SerialName("barcode")
    val barcode: String? = null,

    @SerialName("online_quantity")
    val onlineCount: Int? = null,

    @SerialName("retail_quantity")
    val retailCount: Int? = null,
) {
    fun toProductOffer(): ProductOffer? {
        return if (
            id != null
            && size != null
            && isAvailable != null
            && barcode != null
            && onlineCount != null
            && retailCount != null
        ) {
            ProductOffer(
                id = ProductOffer.Id(id),
                size = size,
                sizeRu = sizeRu,
                isAvailable = isAvailable,
                height = height,
                barcode = Barcode(barcode),
                onlineCount = onlineCount,
                retailCount = retailCount,
            )
        } else {
            Timber.e("Drop ProductOffer because its ID, size, isAvailable, barcode, onlineCount or retailCount is null")
            null
        }
    }
}
