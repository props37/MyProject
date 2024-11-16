package ru.livetyping.zarina.data.geography.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.data.geography.impl.remote.GeographyRemoteDataSource
import javax.inject.Inject

internal class GeographyRepositoryImpl @Inject constructor(
    private val remoteDataSource: GeographyRemoteDataSource,
) : GeographyRepository {
    override fun getCityByLocationFlow(location: Location): Flow<City> {
        return remoteDataSource.getCityByLocationFlow(location)
    }
}
