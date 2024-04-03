package ru.livetyping.zarina.data.old.location.source

import ru.livetyping.zarina.domain.old.GeoLocation

interface IGeoLocationSource {

    suspend fun getCurrentLocation(): GeoLocation?

}
