package ru.zarina.zarina.data.geography.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.City
import javax.inject.Inject

class MemoryGeographyLocalSource @Inject constructor() : IGeographyLocalSource {

    private val citiesByQuery = MutableStateFlow(mapOf<String?, List<City>>())

    override suspend fun setCities(name: String?, cities: List<City>) {
        citiesByQuery.update { it + (name to cities) }
    }

    override suspend fun getCities(name: String?) = citiesByQuery.value[name]

}
