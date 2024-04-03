package ru.livetyping.zarina.data.old.geography.remote

import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.GeoLocation

interface IGeographyRemoteSource {
    suspend fun getCity(location: GeoLocation): City?
    suspend fun getCities(name: String?): List<City>
}
