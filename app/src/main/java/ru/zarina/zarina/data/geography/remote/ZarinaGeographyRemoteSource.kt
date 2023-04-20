package ru.zarina.zarina.data.geography.remote

import ru.zarina.zarina.data.geography.remote.api.IZarinaGeographyApi
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation
import javax.inject.Inject

class ZarinaGeographyRemoteSource @Inject constructor(
    private val api: IZarinaGeographyApi,
) : IGeographyRemoteSource {

    override suspend fun getCity(location: GeoLocation): City? {
        return api.getCity(location.latitude, location.longitude).toDomain()
    }

    override suspend fun getCities(name: String?): List<City> {
        return api.getCities(name).mapNotNull { it.toDomain() }
    }

}
