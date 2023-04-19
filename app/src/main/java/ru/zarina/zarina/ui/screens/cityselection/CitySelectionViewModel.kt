package ru.zarina.zarina.ui.screens.cityselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class CitySelectionViewModel @Inject constructor(
    private val interactor: CitySelectionInteractor,
) : ViewModel(),
    ISideEffectSource<CitySelectionViewModel.SideEffect> by SideEffectQueue() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private val _cities = MutableStateFlow<List<CityListItem>>(emptyList())
    val cities: StateFlow<List<CityListItem>> = _cities

    init {
        fetchCities(null)
    }

    fun onQueryChange(query: String) {
        _query.value = query
        fetchCities(query)
    }

    private fun fetchCities(query: String?) {
        // TODO cancel previous fetch
        // TODO operation tracking
        viewModelScope.launch {
            interactor.getCities(query)
                .onSuccess {
                    withContext(Dispatchers.IO) {
                        _cities.value = it.toCityListItems()
                    }
                }
                .onFailure { /* TODO display some error */ }
        }
    }

    private fun List<City>.toCityListItems(): List<CityListItem> = buildList {
        var previousStartingLetter: Char? = null
        this@toCityListItems.sortedBy { it.name }.forEach { city ->
            if (city.name.isEmpty()) return@forEach
            if (city.name.first() != previousStartingLetter) {
                previousStartingLetter = city.name.first()
                add(CityListItem.Header(previousStartingLetter.toString()))
            }
            add(CityListItem.Item(city))
        }
    }

    sealed class CityListItem(val key: String, val contentType: String) {
        data class Header(val letter: String) : CityListItem(letter, "header")
        data class Item(val city: City) : CityListItem(city.id.id, "item")
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
