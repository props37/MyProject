package ru.zarina.zarina.data.geography.remote

import ru.zarina.zarina.data.geography.remote.api.IGeographyApi
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation
import javax.inject.Inject

class KtorGeographyRemoteSource @Inject constructor(
    private val api: IGeographyApi,
) : IGeographyRemoteSource {

    override suspend fun getCity(location: GeoLocation): City? {
        return api.getCity(location.latitude, location.longitude).toDomain()
    }

}
