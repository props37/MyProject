package ru.zarina.zarina.data.geography

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.geography.local.IGeographyLocalSource
import ru.zarina.zarina.data.geography.remote.IGeographyRemoteSource
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation
import timber.log.Timber

@Factory
class GeographyRepository(
    private val local: IGeographyLocalSource,
    private val remote: IGeographyRemoteSource,
) : IGeographyRepository {

    override suspend fun getCity(location: GeoLocation) = remote.getCity(location)

    override suspend fun getCities(name: String?): List<City> {
        val cached = local.getCities(name)
        return if (cached != null) {
            Timber.v("Hit cache for name $name")
            cached
        } else {
            val cities = remote.getCities(name)
            local.setCities(name, cities)
            cities
        }
    }

}
