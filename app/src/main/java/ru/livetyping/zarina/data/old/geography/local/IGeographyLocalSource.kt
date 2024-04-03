package ru.livetyping.zarina.data.old.geography.local

import ru.livetyping.zarina.domain.old.City

interface IGeographyLocalSource {
    suspend fun setCities(name: String?, cities: List<City>)
    suspend fun getCities(name: String?): List<City>?
}

