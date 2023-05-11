package ru.zarina.zarina.ui.screens.bases.selectcity

import ru.zarina.zarina.domain.City

sealed class CityListItem(val key: String, val contentType: String) {
    data class Header(val letter: String) : CityListItem(letter, "header")
    data class Item(val city: City) : CityListItem(city.id.id, "item")
}
