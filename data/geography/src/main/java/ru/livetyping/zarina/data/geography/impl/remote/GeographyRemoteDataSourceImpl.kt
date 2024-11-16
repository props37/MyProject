package ru.livetyping.zarina.data.geography.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.data.geography.impl.remote.api.GeographyApi
import javax.inject.Inject

internal class GeographyRemoteDataSourceImpl @Inject constructor(
    private val api: GeographyApi,
) : GeographyRemoteDataSource {
    override fun getCityByLocationFlow(location: Location): Flow<City> = flow {
        val city = api.getCityByLocation(location).toCity()
        checkNotNull(city) { "city is null" }
        emit(city)
    }
}
