package ru.zarina.zarina.data.rework.geography.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class GeographyLocalDataSource @Inject constructor(
    private val dataHolder: GeographyDataHolder,
) {
    fun setCities(nameQuery: String?, cities: List<City>) {
        dataHolder.setCities(nameQuery, cities)
    }

    fun getCities(nameQuery: String?): Flow<List<City>?> {
        return dataHolder.getCities(nameQuery)
    }
}
