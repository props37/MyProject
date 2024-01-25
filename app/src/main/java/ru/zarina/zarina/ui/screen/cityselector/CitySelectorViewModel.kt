package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.geography.KladrId
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.geography.GetCitiesFlowUseCase
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import ru.zarina.zarina.domain.rework.geography.City as DomainCity

@HiltViewModel
class CitySelectorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: CitySelectorInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var fetchCitiesJob: Job? = null

    private val initialCity: StateFlow<City?> = savedStateHandle
        .getStateFlow<CityParcelable?>(
            key = UnscopedDestinations.CitySelector.ARG_KEY_CITY,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toCity() }

    val selectedCity: StateFlow<City?> = savedStateHandle
        .getStateFlow(
            key = KEY_SELECTED_CITY,
            initialValue = initialCity.value?.let { CityParcelable.fromCity(it) },
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toCity() }

    private val hasSelectedCityChanged = MutableStateFlow(false)

    val cityNameQuery: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_CITY_NAME_QUERY,
        initialValue = "",
    )

    private val _cityListState = MutableStateFlow<CityListState>(CityListState.Loading)
    val cityListState: StateFlow<CityListState> = _cityListState.asStateFlow()

    val isCitySearchBarVisible: StateFlow<Boolean> = cityListState.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it is CityListState.CityList }

    val isChangeCityButtonVisible: StateFlow<Boolean> = hasSelectedCityChanged.asStateFlow()

    init {
        fetchCities(cityNameQuery = null)
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val result = CitySelectorScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onCityNameQueryChanged(query: String) {
        savedStateHandle[KEY_CITY_NAME_QUERY] = query
        fetchCities(query, delay = SEARCH_CITIES_BY_NAME_DELAY)
    }

    fun onCitySearchBarClearClicked() {
        onCityNameQueryChanged(query = "")
    }

    fun onCitySearchBarCancelClicked() {
        emitSideEffect(SideEffect.FreeCitySearchBarFocus)
    }

    fun onCityClicked(city: City) {
        val cityParcelable = CityParcelable.fromCity(city)
        savedStateHandle[KEY_SELECTED_CITY] = cityParcelable
        if (city.kladrId != initialCity.value?.kladrId) {
            hasSelectedCityChanged.value = true
        }
    }

    fun onChangeCityClicked() {
        val city = selectedCity.value ?: return
        navigationThrottler.throttle {
            val result = CitySelectorScreenResult.CitySelected(city)
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onErrorRefreshClicked() {
        _cityListState.value = CityListState.Loading
        fetchCities(cityNameQuery.value)
    }

    // TODO: [Medium] Migrate to Flow APIs to not collect Flows without considering UI lifecycle. See CatalogViewModel as example
    private fun fetchCities(cityNameQuery: String?, delay: Duration = Duration.ZERO) {
        fetchCitiesJob?.cancel()
        fetchCitiesJob = viewModelScope.launch {
            delay(delay)
            val getCitiesParams = GetCitiesFlowUseCase.Params(cityNameQuery)
            interactor.getCitiesFlow(getCitiesParams).collect { result ->
                val cityListState = result.fold(
                    onSuccess = { cities ->
                        cityListStateFromFetchCitiesSuccess(cityNameQuery, cities)
                    },
                    onFailure = { throwable ->
                        val errorState = when (throwable) {
                            is IOException -> ErrorStateRework.NETWORK
                            else -> ErrorStateRework.GENERIC
                        }
                        CityListState.Error(errorState)
                    },
                )
                _cityListState.value = cityListState
            }
        }
    }

    private fun cityListStateFromFetchCitiesSuccess(
        cityNameQuery: String?,
        cities: List<City>,
    ): CityListState {
        val listItems = if (cityNameQuery.isNullOrBlank()) {
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
        }.toImmutableList()
        return CityListState.CityList(listItems)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: CitySelectorScreenResult) : SideEffect

        data object FreeCitySearchBarFocus : SideEffect
    }

    @Stable
    sealed class CityListState {
        data object Loading : CityListState()

        @Immutable
        data class CityList(val items: ImmutableList<CityListItem>) : CityListState()

        @Immutable
        data class Error(val errorState: ErrorStateRework) : CityListState()
    }

    @Stable
    sealed class CityListItem {
        // TODO: [Low] Rename to CityItem
        @Immutable
        data class City(
            val city: DomainCity,
            val showFullName: Boolean = false,
        ) : CityListItem()

        // TODO: [Low] Rename to CityFirstLetterHeaderItem
        @Immutable
        data class CityFirstLetterHeader(val letter: Char) : CityListItem()
    }

    companion object {
        private const val KEY_SELECTED_CITY = "selected_city"
        private const val KEY_CITY_NAME_QUERY = "city_name_query"

        private val SEARCH_CITIES_BY_NAME_DELAY = 200.milliseconds

        private val MAIN_CITIES_KLADR_IDS: List<KladrId>
            get() = listOf(KladrId.MOSCOW, KladrId.SAINT_PETERSBURG)
    }
}

