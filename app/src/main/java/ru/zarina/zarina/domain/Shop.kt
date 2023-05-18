package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    val id: String,
    val name: String,
    val geoLocation: GeoLocation,
    val address: String,
    val phone: String,
    val schedule: String,
) : Parcelable
