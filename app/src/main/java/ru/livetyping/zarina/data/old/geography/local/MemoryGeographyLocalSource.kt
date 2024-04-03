package ru.livetyping.zarina.data.old.geography.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.City

@Factory
class MemoryGeographyLocalSource : IGeographyLocalSource {

    private val citiesByQuery = MutableStateFlow(mapOf<String?, List<City>>())

    override suspend fun setCities(name: String?, cities: List<City>) {
        citiesByQuery.update { it + (name to cities) }
    }

    override suspend fun getCities(name: String?) = citiesByQuery.value[name]

}
