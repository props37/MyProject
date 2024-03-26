package ru.zarina.zarina.data.old.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.old.ApiContract
import ru.zarina.zarina.domain.old.Country

@Serializable
data class ShopCountryDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("cities")
    val cities: List<ShopCityDto>? = null,
) {
    fun toDomain(): Country? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        )
            Country(
                id = id.toString(),
                name = name,
                cities = cities?.mapNotNull { it.toDomain() }.orEmpty()
            )
        else
            null
    }
}
