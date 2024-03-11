package ru.zarina.zarina.data.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.domain.rework.location.Location
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val dataSource: LocationDataSource,
) {
    fun getCurrentLocationFlow(): Flow<Location?> = flow {
        emit(dataSource.getCurrentLocation())
    }
}
