package ru.zarina.zarina.data.old.location.source

import ru.zarina.zarina.domain.GeoLocation

interface IGeoLocationSource {

    suspend fun getCurrentLocation(): GeoLocation?

}
