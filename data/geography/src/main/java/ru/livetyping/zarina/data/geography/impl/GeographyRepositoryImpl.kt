package ru.livetyping.zarina.data.geography.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.geo.Street
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

    override fun getCitiesFlow(nameQuery: String?, cachePolicy: CachePolicy): Flow<List<City>> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getCitiesFlow(nameQuery).filterNotNull()
            is CachePolicy.LocalFirstThenRemote -> {
                getCitiesFlowLocalFirstThenRemote(nameQuery, cachePolicy)
            }

            is CachePolicy.Remote -> getCitiesFlowRemote(nameQuery, cachePolicy)
        }
    }

    override fun getCityStreetsFlow(cityKladrId: KladrId, nameQuery: String): Flow<List<Street>> {
        return remoteDataSource.getCityStreetsFlow(cityKladrId, nameQuery)
    }

    override fun getStreetBuildings(
        streetKladrId: KladrId,
        nameQuery: String,
    ): Flow<List<Building>> {
        return remoteDataSource.getStreetBuildings(streetKladrId, nameQuery)
    }

    private fun getCitiesFlowLocalFirstThenRemote(
        nameQuery: String?,
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<List<City>> {
        return localDataSource.getCitiesFlow(nameQuery).map { cached ->
            if (cached != null) {
                cached
            } else {
                val cities = remoteDataSource.getCitiesFlow(nameQuery).firstOrNull()
                checkNotNull(cities) { "Failed to fetch cities for name query \"$nameQuery\"" }
                citiesCacheUpdatePolicyImpl(nameQuery, cities, cachePolicy.updatePolicy)
                cities
            }
        }
    }

    private fun getCitiesFlowRemote(
        nameQuery: String?,
        cachePolicy: CachePolicy.Remote,
    ): Flow<List<City>> {
        return remoteDataSource.getCitiesFlow(nameQuery)
            .onEach { cities ->
                citiesCacheUpdatePolicyImpl(nameQuery, cities, cachePolicy.updatePolicy)
            }
    }

    private fun citiesCacheUpdatePolicyImpl(
        nameQuery: String?,
        cities: List<City>,
        policy: CacheUpdatePolicy,
    ) {
        when (policy) {
            CacheUpdatePolicy.NONE -> Unit
            CacheUpdatePolicy.CLEAR -> localDataSource.setCities(nameQuery, emptyList())
            CacheUpdatePolicy.UPDATE -> localDataSource.setCities(nameQuery, cities)
        }
    }

    private companion object {
        private const val TAG = "GeographyRepositoryImpl"
    }
}
