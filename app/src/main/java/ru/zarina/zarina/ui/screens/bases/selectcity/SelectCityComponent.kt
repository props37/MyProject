package ru.zarina.zarina.ui.screens.bases.selectcity

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.old.City

@Factory
class SelectCityComponent {

    fun List<City>.toCityListItems(
        priorityCitiesAtTop: Boolean,
        selectedCity: City? = null,
    ): ImmutableList<CityListItem> = buildList {
        var previousStartingLetter: Char? = null
        val (priorityCities, regularCities) = if (priorityCitiesAtTop)
            this@toCityListItems.partition { it.priority != null }
        else
            emptyList<City>() to this@toCityListItems

        addAll(priorityCities.sortedBy { it.priority }
            .map { CityListItem.Item(it, isSelected = it.id == selectedCity?.id) })

        regularCities.sortedBy { it.name }.forEach { city ->
            if (city.name.isEmpty()) return@forEach
            if (city.name.first() != previousStartingLetter) {
                previousStartingLetter = city.name.first()
                add(CityListItem.Header(previousStartingLetter.toString()))
            }
            add(CityListItem.Item(city, isSelected = city.id == selectedCity?.id))
        }
    }.toPersistentList()

    sealed class CityListItem(val key: String, val contentType: String) {
        data class Header(val letter: String) : CityListItem(letter, "header")
        data class Item(val city: City, val isSelected: Boolean = false) :
            CityListItem(city.id.id, "item")
    }

    enum class ErrorType { NETWORK, NO_RESULTS, GENERIC }

}
