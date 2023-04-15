package ru.zarina.zarina.data.location.source

import ru.zarina.zarina.domain.GeoLocation
import javax.inject.Inject

class PlayServicesGeoLocationSource @Inject constructor() : IGeoLocationSource {

    override suspend fun getLocation(): GeoLocation {
        TODO("Not yet implemented")
    }

}
