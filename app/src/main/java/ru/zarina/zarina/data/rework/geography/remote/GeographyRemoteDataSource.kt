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
        return api.getCity(location).toCity()
    }

    fun getCities(nameQuery: String?): Flow<List<City>> {
        return flow {
            val cities = api.getCities(nameQuery).map { it.toCity() }
            emit(cities)
        }
    }

    suspend fun updateUserCity(city: City) {
        api.updateUserCity(city)
    }
}
