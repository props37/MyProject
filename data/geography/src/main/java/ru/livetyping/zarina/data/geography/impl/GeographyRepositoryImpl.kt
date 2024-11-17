package ru.livetyping.zarina.data.geography.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.data.geography.impl.local.GeographyLocalDataSource
import ru.livetyping.zarina.data.geography.impl.remote.GeographyRemoteDataSource
import javax.inject.Inject

internal class GeographyRepositoryImpl @Inject constructor(
    private val remoteDataSource: GeographyRemoteDataSource,
    private val localDataSource: GeographyLocalDataSource,
) : GeographyRepository {
    override fun getCityByLocationFlow(location: Location): Flow<City> {
        return remoteDataSource.getCityByLocationFlow(location)
    }

    override fun getCitiesFlow(nameQuery: String?): Flow<List<City>> {
        return localDataSource.getCitiesFlow(nameQuery)
            .onEach { cached ->
                if (cached == null) {
                    val cities = remoteDataSource.getCitiesFlow(nameQuery).firstOrNull()
                    checkNotNull(cities) { "Failed to fetch cities for query $nameQuery" }
                    localDataSource.setCities(nameQuery, cities)
                }
            }
            .filterNotNull()
    }
}
