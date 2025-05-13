package ru.livetyping.zarina.data.geography.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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

    override suspend fun getCities(nameQuery: String?, cachePolicy: CachePolicy): List<City> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> {
                val cached = localDataSource.getCitiesFlow(nameQuery).firstOrNull()
                checkNotNull(cached) { getCityFetchingErrorMessage(nameQuery) }
                cached
            }

            is CachePolicy.LocalFirstThenRemote -> {
                getCitiesLocalFirstThenRemote(nameQuery, cachePolicy)
            }

            is CachePolicy.Remote -> getCitiesRemote(nameQuery, cachePolicy)
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

    private suspend fun getCitiesLocalFirstThenRemote(
        nameQuery: String?,
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): List<City> {
        val cached = localDataSource.getCitiesFlow(nameQuery).firstOrNull()
        return if (cached != null) {
            cached
        } else {
            val cities = remoteDataSource.getCitiesFlow(nameQuery).firstOrNull()
            checkNotNull(cities) { getCityFetchingErrorMessage(nameQuery) }
            citiesCacheUpdatePolicyImpl(nameQuery, cities, cachePolicy.updatePolicy)
            cities
        }
    }

    private suspend fun getCitiesRemote(
        nameQuery: String?,
        cachePolicy: CachePolicy.Remote,
    ): List<City> {
        val cities = remoteDataSource.getCitiesFlow(nameQuery).firstOrNull()
        checkNotNull(cities) { getCityFetchingErrorMessage(nameQuery) }
        citiesCacheUpdatePolicyImpl(nameQuery, cities, cachePolicy.updatePolicy)
        return cities
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

    private fun getCityFetchingErrorMessage(nameQuery: String?): String {
        return if (nameQuery != null) {
            "Failed to fetch cities for name query $nameQuery"
        } else {
            "Failed to fetch cities"
        }
    }

    private companion object {
        private const val TAG = "GeographyRepositoryImpl"
    }
}
