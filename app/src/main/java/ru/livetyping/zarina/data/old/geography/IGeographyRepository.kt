package ru.livetyping.zarina.data.old.geography

import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.GeoLocation

interface IGeographyRepository {
    suspend fun getCity(location: GeoLocation): City?
    suspend fun getCities(name: String?): List<City>
}
