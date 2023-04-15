package ru.zarina.zarina.data.geography.remote

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation

interface IGeographyRemoteSource {
    suspend fun getCity(location: GeoLocation): City?
}
