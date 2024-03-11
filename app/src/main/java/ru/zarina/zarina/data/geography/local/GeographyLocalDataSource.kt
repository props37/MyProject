package ru.zarina.zarina.data.geography.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class GeographyLocalDataSource @Inject constructor(
    private val dataHolder: GeographyDataHolder,
) {
    fun setCities(nameQuery: String?, cities: List<City>) {
        dataHolder.setCities(nameQuery, cities)
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>?> {
        return dataHolder.getCitiesFlow(nameQuery)
    }
}
