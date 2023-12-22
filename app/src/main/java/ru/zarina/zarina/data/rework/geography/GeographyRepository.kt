package ru.zarina.zarina.data.rework.geography

import ru.zarina.zarina.data.rework.geography.remote.GeographyRemoteDataSource
import ru.zarina.zarina.domain.rework.Location
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class GeographyRepository @Inject constructor(
    private val remoteDataSource: GeographyRemoteDataSource,
) {
    suspend fun getCity(location: Location): City? {
        return remoteDataSource.getCity(location)
    }
}
