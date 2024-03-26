package ru.zarina.zarina.data.old.location

import ru.zarina.zarina.domain.old.GeoLocation

interface IGeoLocationRepository {

    suspend fun getCurrentLocation(): GeoLocation?

}
