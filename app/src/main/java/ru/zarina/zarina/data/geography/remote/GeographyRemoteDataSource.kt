package ru.zarina.zarina.data.geography.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.geography.remote.api.GeographyApi
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.domain.location.Location
import javax.inject.Inject

class GeographyRemoteDataSource @Inject constructor(
    private val api: GeographyApi,
) {
    suspend fun getCityFlow(location: Location): Flow<City> = flow {
        val city = api.getCity(location).toCity()
        checkNotNull(city) { "city is null" }
        emit(city)
    }

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>> = flow {
        val cities = api.getCities(nameQuery).mapNotNull { it.toCity() }
        emit(cities)
    }
}
