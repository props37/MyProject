package ru.livetyping.zarina.data.geography.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.BuildingDto
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.StreetDto

internal interface GeographyApi {
    suspend fun getCityByLocation(location: Location): CityDto

    suspend fun getCities(nameQuery: String?): List<CityDto>

    suspend fun getCityStreets(cityKladrId: KladrId, nameQuery: String): List<StreetDto>

    suspend fun getStreetBuildings(streetKladrId: KladrId, nameQuery: String): List<BuildingDto>
}
