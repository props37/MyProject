package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.usecase.geo.GetCitiesUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorState
import javax.inject.Inject

@HiltViewModel
internal class CitySelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCities: GetCitiesUseCase,
    private val setUserCity: SetUserCityUseCase,
) : ViewModel(), SideEffectSource<CitySelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var fetchCitiesJob: Job? = null
    private var changeCityJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<CitySelectorFeature.NavEntry>(
        typeMap = CitySelectorFeature.NavEntry.typeMap(),
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val nameQueryTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val cityResult = MutableStateFlow<Result<List<City>>?>(null)

    private val selectedCity = MutableStateFlow(navEntry.currentCity?.toCity())

    private val hasSelectedCityChanged = MutableStateFlow(false)

    private val cityListStateBuilder = CityListState.Builder()
    val citySelectorState: StateFlow<CitySelectorState> = combine(
        cityResult,
        selectedCity,
        hasSelectedCityChanged,
        operationTracker.ongoingOperationKeys,
    ) { cityResult, selectedCity, hasSelectedCityChanged, ongoingOperations ->
        val cityListState = cityListStateBuilder.build(
            cityResult = cityResult,
            isLoadingCities = Operation.FetchCities in ongoingOperations,
            selectedCity = selectedCity,
            priorityCityKladrIds = PRIORITY_CITY_KLADR_ID_SET,
            isChangeCityButtonVisible = hasSelectedCityChanged,
            isChangeCityButtonLoading = Operation.ChangeCity in ongoingOperations,
        )

        CitySelectorState(
            citySearchTextFieldState = nameQueryTextFieldState,
            cityListState = cityListState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CitySelectorState(
            citySearchTextFieldState = nameQueryTextFieldState,
            cityListState = CityListState.Loading,
        ),
    )

    private val cityCachePolicy = CachePolicy.LocalFirstThenRemote()

    init {
        fetchCitiesOnNameQueryChange()
    }

    fun onCitySelectorEvent(event: CitySelectorEvent) {
        when (event) {
            CitySelectorEvent.CloseClicked -> onCloseClicked()
            is CitySelectorEvent.CitySelected -> onCitySelected(event)
            CitySelectorEvent.RefreshClicked -> onRefreshClicked()
            CitySelectorEvent.ChangeCityClicked -> onChangeCityClicked()
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CitySelectorScreenAction.BackClicked
            emitSideEffect(CitySelectorSideEffect.Navigate(action))
        }
    }

    private fun onCitySelected(event: CitySelectorEvent.CitySelected) {
        val city = event.city
        selectedCity.value = city

        val initialCity = navEntry.currentCity?.toCity()
        if (city != initialCity) {
            hasSelectedCityChanged.value = true
        }
    }

    private fun onRefreshClicked() {
        viewModelScope.launch {
            val nameQuery = nameQueryTextFieldState.text.toString()
            fetchCities(nameQuery)
        }
    }

    private fun onChangeCityClicked() {
        val city = selectedCity.value
        if (city == null || changeCityJob?.isActive == true) return

        changeCityJob = viewModelScope.launch {
            operationTracker.track(Operation.ChangeCity) {
                val params = SetUserCityUseCase.Params(city)
                setUserCity(params)
                    .onSuccess {
                        onCityChanged(city)
                    }
                    .onFailure(::onCityChangeFailure)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun fetchCitiesOnNameQueryChange() {
        nameQueryTextFieldState.textAsFlow()
            .debounce { query ->
                if (query.isNotBlank()) CITY_SEARCH_DEBOUNCE_DELAY_MILLIS else 0
            }
            .transformLatest<CharSequence, Unit> { query ->
                fetchCities(query.toString())
            }
            .launchIn(viewModelScope)
    }

    private suspend fun fetchCities(nameQuery: String) {
        fetchCitiesJob?.cancel()

        coroutineScope {
            fetchCitiesJob = launch {
                operationTracker.track(Operation.FetchCities) {
                    val params = GetCitiesUseCase.Params(nameQuery, cityCachePolicy)
                    cityResult.value = getCities(params)
                }
            }
        }
    }

    private fun onCityChanged(city: City) {
        val action = CitySelectorScreenAction.CitySelected(city)
        emitSideEffect(CitySelectorSideEffect.Navigate(action))
    }

    private fun onCityChangeFailure(t: Throwable) {
        // TODO: [Top] Implement
    }

    private enum class Operation : OperationKey { FetchCities, ChangeCity }

    private companion object {
        const val CITY_SEARCH_DEBOUNCE_DELAY_MILLIS = 300L

        val PRIORITY_CITY_KLADR_ID_SET: Set<KladrId>
            get() = setOf(KladrId.MOSCOW, KladrId.SAINT_PETERSBURG)
    }
}
