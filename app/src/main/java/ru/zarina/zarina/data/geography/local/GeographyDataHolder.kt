package ru.zarina.zarina.data.geography.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.rework.geography.City
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeographyDataHolder @Inject constructor() {
    private val citiesToNameQuery = MutableStateFlow(mapOf<String?, List<City>>())

    fun setCities(nameQuery: String?, cities: List<City>) {
        Timber.v("Set cities for name query $nameQuery: $cities")
        citiesToNameQuery.update { it + (nameQuery to cities) }
        ensureCitiesToNameQueryCacheSize()
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>?> {
        return citiesToNameQuery.map { map ->
            map[nameQuery]
        }
    }

    private fun ensureCitiesToNameQueryCacheSize() {
        val cachedNameQueries = citiesToNameQuery.value.keys
        if (cachedNameQueries.size > MAX_CACHED_CITY_NAME_QUERIES) {
            val nameQueryToRemove = cachedNameQueries.firstOrNull { it != null }
            if (nameQueryToRemove != null) {
                Timber.v("Clear cached cities for name query $nameQueryToRemove")
                citiesToNameQuery.update { it - nameQueryToRemove }
            }
        }
    }

    companion object {
        private const val MAX_CACHED_CITY_NAME_QUERIES = 5
    }
}
