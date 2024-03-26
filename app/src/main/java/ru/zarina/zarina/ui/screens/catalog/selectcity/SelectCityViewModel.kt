package ru.zarina.zarina.ui.screens.catalog.selectcity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityComponent
import ru.zarina.zarina.ui.screens.catalog.selectshop.SelectShopViewModel

@KoinViewModel
class SelectCityViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val selectShopSavedStateHandle: SavedStateHandle,
    private val interactor: SelectCityInteractor,
    private val selectCityComponent: SelectCityComponent,
) : ViewModel(),
    ISideEffectSource<SelectCityViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()

    private val selectedCityResult =
        selectShopSavedStateHandle.getStateFlow<Result<City>?>(SelectShopViewModel.KEY_CITY, null)
    private val selectedCity =
        savedStateHandle.getStateFlow(KEY_SELECTED_CITY, selectedCityResult.value?.getOrNull())

    private val citiesResult = MutableStateFlow<Result<List<City>>?>(null)
    val query = savedStateHandle.getStateFlow(KEY_QUERY, "")
    val cities = combine(citiesResult, selectedCity, query) { citiesResult, selectedCity, query ->
        with(selectCityComponent) {
            citiesResult?.getOrNull()
                .orEmpty()
                .filter { it.name.contains(query, true) }
                .sortedBy { it.name }
                .toCityListItems(true, selectedCity)
        }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    val isLoaderVisible = operationTracker.isOperationOngoing(Operation.LOADING_CITIES)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    val isApplyButtonVisible =
        combine(selectedCityResult, selectedCity) { originalSelectedCity, selectedCity ->
            originalSelectedCity?.getOrNull()?.id != selectedCity?.id
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_CITIES) {
                citiesResult.value = interactor.getPickupCities()
            }
        }
    }

    fun onQueryChange(query: String) {
        savedStateHandle[KEY_QUERY] = query
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onCityClick(city: City) {
        savedStateHandle[KEY_SELECTED_CITY] = if (selectedCity.value == city) null else city
    }

    fun onApplyClick() {
        selectShopSavedStateHandle[SelectShopViewModel.KEY_CITY] =
            Result.success(selectedCity.value)
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

    private enum class Operation : OperationKey { LOADING_CITIES }

    companion object {
        private const val KEY_QUERY = "query"
        private const val KEY_SELECTED_CITY = "selected_city"
    }

}
