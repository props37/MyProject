package ru.zarina.zarina.data.rework.geography.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.geography.remote.api.GeographyApi
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.location.Location
import javax.inject.Inject

class GeographyRemoteDataSource @Inject constructor(
    private val api: GeographyApi,
) {
    suspend fun getCity(location: Location): City {
        val city = api.getCity(location).toCity()
        return checkNotNull(city) { "City is null" }
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>> = flow {
        val cities = api.getCities(nameQuery).mapNotNull { it.toCity() }
        emit(cities)
    }
}
