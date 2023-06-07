package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract

@Serializable
data class PriceFilterDto(
    @SerialName("min")
    val min: Int? = null,
    @SerialName("max")
    val max: Int? = null,
) {

    fun toDomain(): IntRange? {
        return if (
            ApiContract.isNotNull(min)
            && ApiContract.isNotNull(max)
        )
            IntRange(min, max)
        else
            null
    }

}
