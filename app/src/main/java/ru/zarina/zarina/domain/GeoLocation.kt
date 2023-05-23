package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
) : Parcelable
