package ru.zarina.zarina.ui.screens.pickup.selectshopcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _cities =
        MutableStateFlow<ImmutableList<SelectCityComponent.CityListItem>>(persistentListOf())
    val cities: StateFlow<ImmutableList<SelectCityComponent.CityListItem>> = _cities

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _errorType = MutableStateFlow<SelectCityComponent.ErrorType?>(null)
    val errorType = _errorType.asStateFlow()

    init {
        loadPickupCities()
    }

    fun onQueryChange(query: String) {
        _query.value = query
        // TODO filter list
    }

    private fun loadPickupCities() {
        viewModelScope.launch {
            // TODO loader
            // TODO errors
            interactor.getPickupCities()
                .onSuccess {
                    _cities.value = with(selectCityComponent) { it.toCityListItems(true) }
                }
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
