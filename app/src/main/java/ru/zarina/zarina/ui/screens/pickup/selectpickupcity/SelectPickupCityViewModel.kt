package ru.zarina.zarina.ui.screens.pickup.selectpickupcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityComponent
import javax.inject.Inject

@HiltViewModel
class SelectPickupCityViewModel @Inject constructor(
    private val interactor: SelectPickupCityInteractor,
    private val selectCityComponent: SelectCityComponent,
) : ViewModel(),
    ISideEffectSource<SelectPickupCityViewModel.SideEffect> by SideEffectQueue() {

    private val _cities = MutableStateFlow<List<City>>(persistentListOf())

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val cities = combine(_cities, _query) { cities, query ->
        with(selectCityComponent) {
            cities
                .filter { city -> city.name.contains(query, ignoreCase = true) }
                .toCityListItems(true)
                .toPersistentList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())

    private val _errorType = MutableStateFlow<SelectCityComponent.ErrorType?>(null)
    val errorType = _errorType.asStateFlow()

    init {
        loadPickupCities()
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    private fun loadPickupCities() {
        viewModelScope.launch {
            // TODO loader
            // TODO errors
            interactor.getPickupCities()
                .onSuccess { _cities.value = it }
        }
    }

    fun onCityClick(city: City) {
        TODO("Not yet implemented")
    }

    fun onErrorButtonClick(errorType: SelectCityComponent.ErrorType) {
        TODO("Not yet implemented")
    }

    fun onCloseClick() {
        TODO("Not yet implemented")
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
