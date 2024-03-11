package ru.zarina.zarina.ui.screens.pickup.selectpickupcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityComponent

@KoinViewModel
class SelectPickupCityViewModel(
    private val interactor: SelectPickupCityInteractor,
    private val selectCityComponent: SelectCityComponent,
) : ViewModel(),
    ISideEffectSource<SelectPickupCityViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()

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

    val isLoaderVisible = operationTracker
        .isOperationOngoing(Operation.LOADING_CITIES)
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        loadPickupCities()
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    private fun loadPickupCities() {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_CITIES) {
                interactor.getPickupCities()
                    .onSuccess { _cities.value = it }
                    .onFailure { sideEffect(SideEffect.ShowError(Text.Resource(R.string.unable_to_load_cities))) }
            }
        }
    }

    fun onCityClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onErrorButtonClick() {
        loadPickupCities()
    }

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        data class ShowError(val message: Text) : SideEffect
    }

    enum class Operation : OperationKey { LOADING_CITIES }

}
