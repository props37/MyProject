package ru.livetyping.zarina.data.geography.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.livetyping.zarina.core.domain.model.geo.City
import timber.log.Timber
import java.lang.ref.SoftReference
import javax.inject.Inject

internal class CityDataHolderImpl @Inject constructor() : CityDataHolder {
    private val nameQueryToCities = MutableStateFlow<SoftReference<Map<String?, List<City>>>?>(null)

    override fun getCitiesFlow(nameQuery: String?): Flow<List<City>?> {
        return nameQueryToCities.map { ref ->
            val map = ref?.get()
            map?.get(nameQuery)
        }
    }

    override fun setCities(nameQuery: String?, cities: List<City>) {
        nameQueryToCities.update { ref ->
            val currentMap = ref?.get() ?: emptyMap()
            val newMap = currentMap + (nameQuery to cities)
            SoftReference(newMap)
        }
        Timber.tag(TAG).v("Cities $cities set for \"$nameQuery\"")
        ensureCityCacheSize()
    }

    private fun ensureCityCacheSize() {
        val cachedNameQueries = nameQueryToCities.value?.get()?.keys ?: return
        if (cachedNameQueries.size > MAX_CACHED_CITY_NAME_QUERIES) {
            val nameQueryToRemove = cachedNameQueries.firstOrNull { it != null }
            if (nameQueryToRemove != null) {
                nameQueryToCities.update { ref ->
                    val currentMap = ref?.get() ?: emptyMap()
                    val newMap = currentMap - nameQueryToRemove
                    SoftReference(newMap)
                }
                Timber.tag(TAG).v("Cached cities cleared for name query \"$nameQueryToRemove\"")
            }
        }
    }

    private companion object {
        private const val MAX_CACHED_CITY_NAME_QUERIES = 5

        private const val TAG = "CityDataHolderImpl"
    }
}
