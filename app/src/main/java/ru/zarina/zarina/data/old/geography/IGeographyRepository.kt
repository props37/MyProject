package ru.zarina.zarina.data.old.geography

import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.GeoLocation

interface IGeographyRepository {
    suspend fun getCity(location: GeoLocation): City?
    suspend fun getCities(name: String?): List<City>
}
