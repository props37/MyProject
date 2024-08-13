package ru.livetyping.zarina.presentation.model.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.location.Location

@Serializable
@Parcelize
data class LocationParcelable(
    val latitude: Double,
    val longitude: Double,
) : Parcelable {
    fun toLocation(): Location {
        return Location(
            latitude = latitude,
            longitude = longitude,
        )
    }

    companion object {
        fun from(location: Location): LocationParcelable {
            return LocationParcelable(
                latitude = location.latitude,
                longitude = location.longitude,
            )
        }
    }
}
