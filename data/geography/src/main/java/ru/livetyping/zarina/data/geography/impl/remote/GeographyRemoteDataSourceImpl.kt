package ru.livetyping.zarina.data.geography.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.geo.Street
import ru.livetyping.zarina.data.geography.impl.remote.api.GeographyApi
import javax.inject.Inject

internal class GeographyRemoteDataSourceImpl @Inject constructor(
    private val api: GeographyApi,
) : GeographyRemoteDataSource {
    override fun getCityByLocationFlow(location: Location): Flow<City> = flow {
        val city = api.getCityByLocation(location).toCity()
        checkNotNull(city) { "city is null" }
        emit(city)
    }

    override fun getCitiesFlow(nameQuery: String?): Flow<List<City>> = flow {
        val cities = api.getCities(nameQuery).mapNotNull { it.toCity() }
        emit(cities)
    }

    override fun getCityStreetsFlow(cityFiasId: FiasId, nameQuery: String): Flow<List<Street>> = flow {
        val dto = api.getCityStreets(cityFiasId, nameQuery)
        val streets = dto.mapNotNull { it.toStreet() }
        emit(streets)
    }

    override fun getStreetBuildings(
        streetFiasId: FiasId,
        nameQuery: String,
    ): Flow<List<Building>> = flow {
        val dto = api.getStreetBuildings(streetFiasId, nameQuery)
        val buildings = dto.mapNotNull { it.toBuilding() }
        emit(buildings)
    }
}
