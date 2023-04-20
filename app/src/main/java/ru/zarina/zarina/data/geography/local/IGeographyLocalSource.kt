package ru.zarina.zarina.data.geography.local

import ru.zarina.zarina.domain.City

interface IGeographyLocalSource {
    suspend fun setCities(name: String?, cities: List<City>)
    suspend fun getCities(name: String?): List<City>?
}

