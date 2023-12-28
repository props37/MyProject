package ru.zarina.zarina.data.rework.location

import ru.zarina.zarina.domain.rework.location.Location
import kotlin.time.Duration.Companion.seconds

interface LocationDataSource {
    suspend fun getCurrentLocation(): Location?

    companion object {
        val CURRENT_LOCATION_DETECTING_TIMEOUT = 5.seconds
    }
}
