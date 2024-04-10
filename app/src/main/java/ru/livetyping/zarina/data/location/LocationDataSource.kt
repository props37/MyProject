package ru.livetyping.zarina.data.location

import ru.livetyping.zarina.domain.location.Location
import kotlin.time.Duration.Companion.seconds

interface LocationDataSource {
    suspend fun getCurrentLocation(): Location?

    companion object {
        val CURRENT_LOCATION_DETECTING_TIMEOUT = 5.seconds
    }
}
