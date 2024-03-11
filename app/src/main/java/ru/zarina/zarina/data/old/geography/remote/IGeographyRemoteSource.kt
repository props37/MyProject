package ru.zarina.zarina.data.old.geography.remote

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation

interface IGeographyRemoteSource {
    suspend fun getCity(location: GeoLocation): City?
    suspend fun getCities(name: String?): List<City>
}
