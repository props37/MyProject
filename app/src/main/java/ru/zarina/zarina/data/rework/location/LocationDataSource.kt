package ru.zarina.zarina.data.rework.location

import ru.zarina.zarina.domain.geography.Location

interface LocationDataSource {
    suspend fun getCurrentLocation(): Location?
}
