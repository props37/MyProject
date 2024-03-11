package ru.zarina.zarina.data.old.location

import ru.zarina.zarina.domain.GeoLocation

interface IGeoLocationRepository {

    suspend fun getCurrentLocation(): GeoLocation?

}
