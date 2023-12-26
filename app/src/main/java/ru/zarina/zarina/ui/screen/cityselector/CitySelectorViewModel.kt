package ru.zarina.zarina.ui.screen.cityselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.geography.KladrId
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.geography.GetCitiesUseCase
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CitySelectorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: CitySelectorInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val initialCity: StateFlow<City?> = savedStateHandle
        .getStateFlow<CityParcelable?>(
            key = UnscopedDestinations.CitySelector.ARG_KEY_CITY,
            initialValue = null,
        )
        .mapState(viewModelScope) { it?.toCity() }

    val cityNameQuery: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_CITY_NAME_QUERY,
        initialValue = "",
    )

    // TODO: [High] Refactor
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val cityListState: StateFlow<CityListState> = cityNameQuery
        .debounce { nameQuery ->
            if (nameQuery.isBlank()) Duration.ZERO else 300.milliseconds
        }
        .flatMapLatest { nameQuery ->
            val getCitiesParams = GetCitiesUseCase.Params(nameQuery)
            interactor.getCities(getCitiesParams).map { result ->
                result.fold(
                    onSuccess = { cities ->
                        val listItems = if (nameQuery.isBlank()) {
                            buildList<CityListItem> {
                                // Show main cities at the top
                                val (mainCities, otherCities) = cities.partition { city ->
                                    city.kladrId in MAIN_CITIES_KLADR_IDS
                                }
                                val mainCityItems = mainCities.map { CityListItem.City(it) }
                                addAll(mainCityItems)

                                // Show other cities grouped by the first letter
                                val otherCitiesGrouped = otherCities.groupBy { city ->
                                    city.name.firstOrNull()
                                }
                                otherCitiesGrouped.forEach { (firstLetter, cities) ->
                                    if (firstLetter != null) {
                                        add(CityListItem.CityFirstLetterHeader(firstLetter))
                                    }
                                    val cityItems = cities.map { CityListItem.City(it) }
                                    addAll(cityItems)
                                }
                            }
                        } else {
                            cities.map { CityListItem.City(it, showFullName = true) }
                        }
                        CityListState.CityList(listItems)
                    },
                    onFailure = { throwable ->
                        CityListState.Error(throwable)
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope + Dispatchers.Default,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CityListState.InitialLoading,
        )

    val selectedCity: StateFlow<City?> = savedStateHandle
        .getStateFlow(
            key = KEY_SELECTED_CITY,
            initialValue = initialCity.value?.let { CityParcelable.fromCity(it) },
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toCity() }

    val isChangeCityButtonVisible: StateFlow<Boolean> = combine(
        initialCity,
        selectedCity,
        cityListState,
    ) { initialCity, selectedCity, cityListState ->
        val visibleCities = (cityListState as? CityListState.CityList)
            ?.list
            ?.mapNotNull { listItem ->
                (listItem as? CityListItem.City)?.city
            }
            ?: emptyList()
        selectedCity?.kladrId != initialCity?.kladrId && selectedCity in visibleCities
    }.stateIn(
        scope = viewModelScope + Dispatchers.Default,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false,
    )

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val result = CitySelectorScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onCityNameQueryChanged(query: String) {
        savedStateHandle[KEY_CITY_NAME_QUERY] = query
    }

    fun onCitySearchBarCancelClicked() {
        emitSideEffect(SideEffect.FreeCitySearchBarFocus)
    }

    fun onCityClicked(city: City) {
        val cityParcelable = CityParcelable.fromCity(city)
        savedStateHandle[KEY_SELECTED_CITY] = cityParcelable
    }

    fun onChangeCityClicked() {
        val city = selectedCity.value ?: return
        navigationThrottler.throttle {
            val result = CitySelectorScreenResult.CitySelected(city)
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: CitySelectorScreenResult) : SideEffect

        data object FreeCitySearchBarFocus : SideEffect
    }

    sealed class CityListState {
        data object InitialLoading : CityListState()

        data class CityList(val list: List<CityListItem>) : CityListState()

        data class Error(val throwable: Throwable) : CityListState()
    }

    sealed class CityListItem {
        data class City(
            val city: ru.zarina.zarina.domain.rework.geography.City,
            val showFullName: Boolean = false,
        ) : CityListItem()

        data class CityFirstLetterHeader(val letter: Char) : CityListItem()
    }

    companion object {
        private const val KEY_SELECTED_CITY = "selected_city"
        private const val KEY_CITY_NAME_QUERY = "city_name_query"

        private val MAIN_CITIES_KLADR_IDS = listOf(KladrId.MOSCOW, KladrId.SAINT_PETERSBURG)
    }
}

