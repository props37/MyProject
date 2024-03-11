package ru.zarina.zarina.data.old.location

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.location.source.IGeoLocationSource

@Factory
class GeoLocationRepository(
    private val source: IGeoLocationSource,
) : IGeoLocationRepository {
    override suspend fun getCurrentLocation() = source.getCurrentLocation()
}

