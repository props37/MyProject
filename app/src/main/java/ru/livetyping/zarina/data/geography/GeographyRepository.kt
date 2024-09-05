package ru.livetyping.zarina.data.geography

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.geography.local.GeographyLocalDataSource
import ru.livetyping.zarina.data.geography.remote.GeographyRemoteDataSource
import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.geography.Street
import ru.livetyping.zarina.domain.location.Location
import javax.inject.Inject

class GeographyRepository @Inject constructor(
    private val remoteDataSource: GeographyRemoteDataSource,
    private val localDataSource: GeographyLocalDataSource,
) {
    suspend fun getCityFlow(location: Location): Flow<City> {
        return remoteDataSource.getCityFlow(location)
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>> {
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

    fun getCityStreetsFlow(cityKladrId: KladrId, nameQuery: String): Flow<List<Street>> {
        return remoteDataSource.getCityStreetsFlow(cityKladrId, nameQuery)
    }

    fun getStreetBuildings(streetKladrId: KladrId, nameQuery: String): Flow<List<Building>> {
        return remoteDataSource.getStreetBuildings(streetKladrId, nameQuery)
    }
}
