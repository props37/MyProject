package ru.livetyping.zarina.data.geography.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.network.zarina.dto.CityDto

internal interface GeographyApi {
    suspend fun getCityByLocation(location: Location): CityDto

    suspend fun getCities(nameQuery: String?): List<CityDto>
}
