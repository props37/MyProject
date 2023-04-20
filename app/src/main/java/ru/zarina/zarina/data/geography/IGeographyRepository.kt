package ru.zarina.zarina.data.geography

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation

interface IGeographyRepository {
    suspend fun getCity(location: GeoLocation): City?
    suspend fun getCities(name: String?): List<City>
}
