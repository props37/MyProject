package ru.livetyping.zarina.domain.location

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val MOSCOW: Location get() = Location(latitude = 55.7558, longitude = 37.6173)
    }
}
