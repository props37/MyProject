package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.PriceRange

@Serializable
data class PriceFilterDto(
    @SerialName("min")
    val min: Int? = null,
    @SerialName("max")
    val max: Int? = null,
) {

    fun toDomain(): PriceRange? {
        return if (
            ApiContract.isNotNull(min)
            && ApiContract.isNotNull(max)
        )
            PriceRange(min, max)
        else
            null
    }

    companion object {
        fun from(priceRange: PriceRange) = PriceFilterDto(priceRange.min, priceRange.max)
    }

}
