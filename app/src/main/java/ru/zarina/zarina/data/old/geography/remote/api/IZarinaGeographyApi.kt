package ru.zarina.zarina.data.old.geography.remote.api

import ru.zarina.zarina.data.old.geography.remote.api.dto.CityDto

interface IZarinaGeographyApi {
    suspend fun getCity(latitude: Double, longitude: Double): CityDto
    suspend fun getCities(name: String?): List<CityDto>
}
