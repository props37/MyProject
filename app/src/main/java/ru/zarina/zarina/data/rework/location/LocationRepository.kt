package ru.zarina.zarina.data.rework.location

import ru.zarina.zarina.domain.geography.Location
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val dataSource: LocationDataSource,
) {
    suspend fun getCurrentLocation(): Location? {
        return dataSource.getCurrentLocation()
    }
}
