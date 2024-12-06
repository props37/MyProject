package ru.livetyping.zarina.core.domain.model.common

// Marked as stable on config/compose/stability_config.txt
public data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    public companion object {
        public val MOSCOW: Location
            get() = Location(latitude = 55.7558, longitude = 37.6173)
    }
}
