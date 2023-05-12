package ru.zarina.zarina.data.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.AddressId
import ru.zarina.zarina.domain.City

@Serializable
data class ShopCityDto(
    @SerialName("kladr_id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("shops")
    val shops: List<ShopDto>? = null,
) {
    fun toDomain(): City? {
        return if (
            ApiContract.isNotNull(id, "kladr_id")
            && ApiContract.isNotNull(name, "name")
        )
            City(
                id = AddressId(id),
                name = name,
                region = null
            )
        else
            null
    }
}
