package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.geo.Street

public interface GeographyRepository {
    public fun getCityByLocationFlow(location: Location): Flow<City>

    public suspend fun getCities(nameQuery: String?, cachePolicy: CachePolicy): List<City>

    public fun getCityStreetsFlow(cityKladrId: KladrId, nameQuery: String): Flow<List<Street>>

    public fun getStreetBuildings(streetKladrId: KladrId, nameQuery: String): Flow<List<Building>>
}
