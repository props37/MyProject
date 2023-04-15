package ru.zarina.zarina.data.location

import ru.zarina.zarina.data.location.source.IGeoLocationSource
import ru.zarina.zarina.domain.GeoLocation
import javax.inject.Inject

class GeoLocationRepository @Inject constructor(
    private val source: IGeoLocationSource,
) : IGeoLocationRepository {
    override suspend fun getCurrentLocation(): GeoLocation {
        TODO("Not yet implemented")
    }
}

