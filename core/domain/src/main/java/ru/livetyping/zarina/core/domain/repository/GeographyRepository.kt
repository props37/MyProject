package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.City

public interface GeographyRepository {
    public fun getCityByLocationFlow(location: Location): Flow<City>

    public fun getCitiesFlow(nameQuery: String?, cachePolicy: CachePolicy): Flow<List<City>>
}
