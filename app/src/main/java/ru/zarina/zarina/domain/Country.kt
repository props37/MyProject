package ru.zarina.zarina.domain

data class Country(
    val id: String,
    val name: String,
    val cities: List<City>,
) {

    val isPickupSupported: Boolean = id == ID_RUSSIA

    companion object {
        private const val ID_RUSSIA = "949"
    }

}
