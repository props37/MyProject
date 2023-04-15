package ru.zarina.zarina.data.location.source

import ru.zarina.zarina.domain.GeoLocation

interface IGeoLocationSource {

    suspend fun getCurrentLocation(): GeoLocation?

}
