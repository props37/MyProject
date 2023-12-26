package ru.zarina.zarina.data.rework.geography

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.geography.remote.GeographyRemoteDataSource
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.location.Location
import javax.inject.Inject

class GeographyRepository @Inject constructor(
    private val remoteDataSource: GeographyRemoteDataSource,
) {
    suspend fun getCity(location: Location): City {
        return remoteDataSource.getCity(location)
    }

    // TODO: [High] Add caching
    fun getCities(nameQuery: String?): Flow<List<City>> {
        return remoteDataSource.getCities(nameQuery)
    }
}
