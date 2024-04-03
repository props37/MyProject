package ru.livetyping.zarina.data.old.location

import ru.livetyping.zarina.domain.old.GeoLocation

interface IGeoLocationRepository {

    suspend fun getCurrentLocation(): GeoLocation?

}
