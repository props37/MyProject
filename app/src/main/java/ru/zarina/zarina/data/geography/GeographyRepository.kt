package ru.zarina.zarina.data.geography

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.geography.local.GeographyLocalDataSource
import ru.zarina.zarina.data.geography.remote.GeographyRemoteDataSource
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.domain.location.Location
import timber.log.Timber
import javax.inject.Inject

class GeographyRepository @Inject constructor(
    private val remoteDataSource: GeographyRemoteDataSource,
    private val localDataSource: GeographyLocalDataSource,
) {
    suspend fun getCityFlow(location: Location): Flow<City> {
        return remoteDataSource.getCityFlow(location)
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>> = flow {
        val cached = localDataSource.getCitiesFlow(nameQuery).firstOrNull()
        if (cached != null) {
            Timber.v("Get cached cities for name query $nameQuery")
            emit(cached)
        } else {
            val cities = remoteDataSource.getCitiesFlow(nameQuery).firstOrNull()
            checkNotNull(cities) { "Failed to fetch cities for query $nameQuery" }
            localDataSource.setCities(nameQuery, cities)
            emit(cities)
        }
    }
}
