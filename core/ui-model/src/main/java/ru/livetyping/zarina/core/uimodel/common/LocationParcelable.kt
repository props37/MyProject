package ru.livetyping.zarina.core.uimodel.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Location

@Serializable
@Parcelize
public data class LocationParcelable(
    val latitude: Double,
    val longitude: Double,
) : Parcelable {
    public fun toLocation(): Location {
        return Location(
            latitude = latitude,
            longitude = longitude,
        )
    }

    public companion object {
        public fun from(location: Location): LocationParcelable {
            return LocationParcelable(
                latitude = location.latitude,
                longitude = location.longitude,
            )
        }
    }
}
