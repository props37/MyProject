package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.location.Location

@Serializable
data class LocationDto(
    @SerialName("lat")
    val latitude: Double? = null,

    @SerialName("lon")
    val longitude: Double? = null,
) {
    fun toLocation(): Location {
        checkNotNull(latitude) { "latitude is null" }
        checkNotNull(longitude) { "longitude is null" }
        return Location(latitude, longitude)
    }
}
