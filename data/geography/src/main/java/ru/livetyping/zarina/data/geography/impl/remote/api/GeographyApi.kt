package ru.livetyping.zarina.data.geography.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.CityDto

internal interface GeographyApi {
    suspend fun getCityByLocation(location: Location): CityDto
}
