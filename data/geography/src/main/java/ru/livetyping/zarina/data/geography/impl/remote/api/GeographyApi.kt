package ru.livetyping.zarina.data.geography.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.BuildingDto
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.StreetDto

internal interface GeographyApi {
    suspend fun getCityByLocation(location: Location): CityDto

    suspend fun getCities(nameQuery: String?): List<CityDto>

    suspend fun getCityStreets(cityFiasId: FiasId, nameQuery: String): List<StreetDto>

    suspend fun getStreetBuildings(streetFiasId: FiasId, nameQuery: String): List<BuildingDto>
}
