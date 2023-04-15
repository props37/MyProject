package ru.zarina.zarina.data.location

import ru.zarina.zarina.data.location.source.IGeoLocationSource
import javax.inject.Inject

class GeoLocationRepository @Inject constructor(
    private val source: IGeoLocationSource,
) : IGeoLocationRepository {
    override suspend fun getCurrentLocation() = source.getCurrentLocation()
}

