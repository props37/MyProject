package ru.livetyping.zarina.data.geography.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import javax.inject.Inject

internal class GeographyLocalDataSourceImpl @Inject constructor(
    private val cityDataHolder: CityDataHolder,
) : GeographyLocalDataSource {
    override fun getCitiesFlow(nameQuery: String?): Flow<List<City>?> {
        return cityDataHolder.getCitiesFlow(nameQuery)
    }

    override fun setCities(nameQuery: String?, cities: List<City>) {
        cityDataHolder.setCities(nameQuery, cities)
    }
}
