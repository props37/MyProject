package ru.zarina.zarina.data.common.remote.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Size

@Serializable
data class SizeDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("size_ru")
    val localSize: String? = null,
    @SerialName("is_available")
    val isAvailable: Boolean? = null,
    @SerialName("offer_id")
    val offerId: String? = null,
    @SerialName("offer_barcode")
    val offerBarcode: String? = null,
) {

    fun toDomain(): Offer? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
            && ApiContract.isNotNull(offerId, "offer_id")
            && ApiContract.isNotNull(offerBarcode, "offer_barcode")
        ) {
            val size = Size(
                id = id,
                name = name
            )
            Offer(
                id = Offer.Id(offerId),
                isAvailable = isAvailable ?: false,
                barcode = offerBarcode,
                size = size,
            )
        } else {
            null
        }
    }

}
