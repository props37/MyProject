package ru.livetyping.zarina.data.location.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import javax.inject.Inject

internal class LocationRepositoryImpl @Inject constructor(
    private val dataSource: LocationDataSource,
) : LocationRepository {
    override fun getCurrentLocationFlow(): Flow<Location?> = flow {
        val location = dataSource.getCurrentLocation()
        emit(location)
    }
}
