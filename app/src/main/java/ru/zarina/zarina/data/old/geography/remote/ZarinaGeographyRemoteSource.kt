package ru.zarina.zarina.data.old.geography.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.geography.remote.api.IZarinaGeographyApi
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.GeoLocation

@Factory
class ZarinaGeographyRemoteSource(
    private val api: IZarinaGeographyApi,
) : IGeographyRemoteSource {

    override suspend fun getCity(location: GeoLocation): City? {
        return api.getCity(location.latitude, location.longitude).toDomain()
    }

    override suspend fun getCities(name: String?): List<City> {
        return api.getCities(name).mapNotNull { it.toDomain() }
    }

}
