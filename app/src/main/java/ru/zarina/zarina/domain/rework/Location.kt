package ru.zarina.zarina.domain.rework

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val SAINT_PETERSBURG = Location(59.937500, 30.308611)
    }
}
