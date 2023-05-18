package ru.zarina.zarina.data.shop.remote.api.dto

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Shop

fun List<ShopCountryDto>.toCountries(): List<Country> {
    return mapNotNull { it.toDomain() }
}

fun List<ShopCountryDto>.toShops(): Map<City, List<Shop>> {
    return buildMap {
        this@toShops.forEach { countryDto ->
            countryDto.cities?.forEach { cityDto ->
                val city = cityDto.toDomain()
                if (city != null)
                    put(city, cityDto.shops?.mapNotNull { it.toDomain() }.orEmpty())
            }
        }
    }
}
