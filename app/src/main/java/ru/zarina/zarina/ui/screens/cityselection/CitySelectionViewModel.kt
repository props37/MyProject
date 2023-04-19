package ru.zarina.zarina.ui.screens.cityselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class CitySelectionViewModel @Inject constructor(
    private val interactor: CitySelectionInteractor,
) : ViewModel(),
    ISideEffectSource<CitySelectionViewModel.SideEffect> by SideEffectQueue() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    init {
        fetchCities(null)
    }

    private fun fetchCities(query: String?) {
        // TODO cancel previous fetch
        // TODO operation tracking
        viewModelScope.launch {
            interactor.getCities(query)
                .onSuccess { _cities.value = it }
                .onFailure { /* TODO display some error */ }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
