package ru.livetyping.zarina.data.old.geography

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.geography.local.IGeographyLocalSource
import ru.livetyping.zarina.data.old.geography.remote.IGeographyRemoteSource
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.GeoLocation
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
