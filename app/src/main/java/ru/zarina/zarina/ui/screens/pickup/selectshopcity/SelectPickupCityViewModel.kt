package ru.zarina.zarina.ui.screens.pickup.selectshopcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SelectPickupCityViewModel @Inject constructor(
    private val interactor: SelectPickupCityInteractor,
) : ViewModel(),
    ISideEffectSource<SelectPickupCityViewModel.SideEffect> by SideEffectQueue() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities = _cities.asStateFlow()

    init {
        loadPickupCities()
    }

    private fun loadPickupCities() {
        viewModelScope.launch {
            // TODO loader
            // TODO errors
            interactor.getPickupCities()
                .onSuccess { _cities.value = it }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
