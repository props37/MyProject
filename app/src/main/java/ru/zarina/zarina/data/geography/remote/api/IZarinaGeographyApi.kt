package ru.zarina.zarina.data.geography.remote.api

import ru.zarina.zarina.data.geography.remote.api.dto.CityDto

interface IZarinaGeographyApi {
    suspend fun getCity(latitude: Double, longitude: Double): CityDto
}
