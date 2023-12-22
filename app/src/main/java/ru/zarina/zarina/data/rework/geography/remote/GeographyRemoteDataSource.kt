package ru.zarina.zarina.data.rework.geography.remote

import ru.zarina.zarina.data.rework.geography.remote.api.GeographyApi
import ru.zarina.zarina.domain.rework.Location
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class GeographyRemoteDataSource @Inject constructor(
    private val api: GeographyApi,
) {
    suspend fun getCity(location: Location): City? {
        TODO("Not yet implemented")
    }
}
