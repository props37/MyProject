package ru.livetyping.zarina.data.old.shop.remote.api.dto

import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.Country
import ru.livetyping.zarina.domain.old.Shop

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
