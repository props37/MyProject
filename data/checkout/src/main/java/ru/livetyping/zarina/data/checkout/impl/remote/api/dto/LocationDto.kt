package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class LocationDto(
    @SerialName("lat")
    val lat: Double? = null,

    @SerialName("lon")
    val lon: Double? = null,
) {
    fun toLocation(): Location {
        checkPropertyNotNull(lat) { ::lat }
        checkPropertyNotNull(lon) { ::lon }
        return Location(lat, lon)
    }
}
