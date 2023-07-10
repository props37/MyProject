package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
) : Parcelable {

    companion object {
        /** Default GeoLocation, the center of Saint-Petersburg */
        val DEFAULT = GeoLocation(59.937500, 30.308611)
    }

}
