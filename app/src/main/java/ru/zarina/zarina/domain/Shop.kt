package ru.zarina.zarina.domain

data class Shop(
    val id: String,
    val name: String,
    val geoLocation: GeoLocation,
    val address: String,
    val phone: String,
    val schedule: String,
)
