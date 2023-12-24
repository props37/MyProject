package ru.zarina.zarina.data.rework.location

import ru.zarina.zarina.domain.rework.location.Location

interface LocationDataSource {
    suspend fun getCurrentLocation(): Location?
}
