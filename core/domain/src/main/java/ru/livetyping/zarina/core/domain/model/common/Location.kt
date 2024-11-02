package ru.livetyping.zarina.core.domain.model.common

public data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    public companion object {
        public val MOSCOW: Location
            get() = Location(latitude = 55.7558, longitude = 37.6173)
    }
}
