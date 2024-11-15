package ru.livetyping.zarina.data.geography.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import javax.inject.Inject

internal class GeographyRepositoryImpl @Inject constructor(

) : GeographyRepository {
    override fun getCityByLocationFlow(location: Location): Flow<City> {
        // TODO: [Top] Implement
        TODO("Not yet implemented")
    }
}
