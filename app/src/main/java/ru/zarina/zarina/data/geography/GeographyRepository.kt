package ru.zarina.zarina.data.geography

import ru.zarina.zarina.data.geography.remote.IGeographyRemoteSource
import ru.zarina.zarina.domain.GeoLocation
import javax.inject.Inject

class GeographyRepository @Inject constructor(
    private val remoteSource: IGeographyRemoteSource,
) : IGeographyRepository {
    override suspend fun getCity(location: GeoLocation) = remoteSource.getCity(location)
}
