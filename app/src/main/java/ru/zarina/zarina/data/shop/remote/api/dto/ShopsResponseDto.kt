package ru.zarina.zarina.data.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Shop

@Serializable
data class ShopsResponseDto(
    @SerialName("shops")
    val shops: List<ShopCountryDto>? = null,
) {

    fun toCountries(): List<Country> {
        return shops?.mapNotNull { it.toDomain() }.orEmpty()
    }

    fun toShops(): Map<City, List<Shop>> {
        return buildMap {
            shops?.forEach { countryDto ->
                countryDto.cities?.forEach { cityDto ->
                    val city = cityDto.toDomain()
                    if (city != null)
                        put(city, cityDto.shops?.mapNotNull { it.toDomain() }.orEmpty())
                }
            }
        }
    }

}
