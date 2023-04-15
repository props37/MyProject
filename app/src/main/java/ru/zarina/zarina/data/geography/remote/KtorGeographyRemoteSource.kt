package ru.zarina.zarina.data.geography.remote

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.GeoLocation

class KtorGeographyRemoteSource() : IGeographyRemoteSource {

    override suspend fun getCity(location: GeoLocation): City? {
        // TODO
        return null
    }

}
