package ru.zarina.zarina.data.old.geography.remote

import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.GeoLocation

interface IGeographyRemoteSource {
    suspend fun getCity(location: GeoLocation): City?
    suspend fun getCities(name: String?): List<City>
}
