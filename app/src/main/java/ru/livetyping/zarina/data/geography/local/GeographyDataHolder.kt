package ru.livetyping.zarina.data.geography.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.livetyping.zarina.domain.geography.City
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeographyDataHolder @Inject constructor() {

    // TODO: [Low] Use SoftReference?
    private val nameQueryToCities = MutableStateFlow(mapOf<String?, List<City>>())

    fun setCities(nameQuery: String?, cities: List<City>) {
        Timber.v("Set cities for name query $nameQuery: $cities")
        nameQueryToCities.update { it + (nameQuery to cities) }
        ensureCityCacheSize()
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>?> {
        return nameQueryToCities.map { map ->
            map[nameQuery]
        }
    }

    private fun ensureCityCacheSize() {
        val cachedNameQueries = nameQueryToCities.value.keys
        if (cachedNameQueries.size > MAX_CACHED_CITY_NAME_QUERIES) {
            val nameQueryToRemove = cachedNameQueries.firstOrNull { it != null }
            if (nameQueryToRemove != null) {
                Timber.v("Clear cached cities for name query $nameQueryToRemove")
                nameQueryToCities.update { it - nameQueryToRemove }
            }
        }
    }

    companion object {
        private const val MAX_CACHED_CITY_NAME_QUERIES = 5
    }
}
