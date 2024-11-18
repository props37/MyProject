package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.usecase.geo.GetCitiesFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavEntry
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitiesForNameQuery
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListStateBuilder
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorState
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class CitySelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCitiesFlowUseCase: GetCitiesFlowUseCase,
) : ViewModel(), SideEffectSource<CitySelectorSideEffect> by SideEffectSourceImpl() {

    // TODO: [High] Inject dispatcher
    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<CitySelectorNavEntry>(
        typeMap = CitySelectorNavEntry.typeMap(),
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val citySearchTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val selectedCityValueHolder = savedStateHandle.createValueHolder(
        key = Keys.SELECTED_CITY.key,
        initialValue = navEntry.currentCity,
    )

    private val hasSelectedCityChanged = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val cityRequester = FlowRequester(CityRequest) { request ->
        citySearchTextFieldState.textAsFlow()
            .debounce { nameQuery ->
                if (nameQuery.isNotBlank()) CITY_SEARCH_DEBOUNCE_DELAY else Duration.ZERO
            }
            .flatMapLatest { nameQuery ->
                markAsLoading(request)
                val nameQueryString = nameQuery.toString()
                val params = GetCitiesFlowUseCase.Params(nameQueryString)
                getCitiesFlowUseCase(params).map { result ->
                    result.map { cities ->
                        CitiesForNameQuery(nameQueryString, cities)
                    }
                }
            }
    }

    val citySelectorState: StateFlow<CitySelectorState> = ReadOnlyStateFlow(
        CitySelectorState(
            title = navEntry.title ?: TITLE_DEFAULT_VALUE,
            citySearchTextFieldState = citySearchTextFieldState,
        )
    )

    private val cityListStateBuilder = CityListStateBuilder()
    private val mainCityKladrIds = MAIN_CITY_KLADR_IDS
    val cityListState: StateFlow<CityListState> = combine(
        cityRequester.flow,
        cityRequester.loadingState,
        selectedCityValueHolder.stateFlow,
        hasSelectedCityChanged,
    ) { citiesForNameQueryResult, citiesLoadingState, selectedCity, hasSelectedCityChanged ->
        cityListStateBuilder.build(
            citiesForNameQueryResult = citiesForNameQueryResult,
            citiesLoadingState = citiesLoadingState,
            selectedCity = selectedCity?.toCity(),
            mainCityKladrIds = mainCityKladrIds,
            isChangeCityButtonVisible = hasSelectedCityChanged,
        )
    }.stateIn(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CityListState.Loading,
    )

    fun onCitySelectorEvent(event: CitySelectorEvent) {
        when (event) {
            CitySelectorEvent.BackClicked -> onBackClicked()
            is CitySelectorEvent.CityClicked -> onCityClicked(event)
            CitySelectorEvent.ChangeCityClicked -> onChangeCityClicked()
            CitySelectorEvent.ErrorRefreshClicked -> cityRequester.request(CityRequest)
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CitySelectorScreenAction.ScreenClosed
            emitSideEffect(CitySelectorSideEffect.Navigate(action))
        }
    }

    private fun onChangeCityClicked() {
        TODO()
        // TODO: [Top] Implement
    }

    private fun onCityClicked(event: CitySelectorEvent.CityClicked) {
        val cityParcelable = CityParcelable.from(event.city)
        selectedCityValueHolder.set(cityParcelable)

        val initialCity = navEntry.currentCity?.toCity()
        if (event.city.id != initialCity?.id) {
            hasSelectedCityChanged.value = true
        }
    }

    private data object CityRequest : FlowRequest

    private enum class Keys {
        SELECTED_CITY;

        val key: String get() = name
    }

    private companion object {
        val TITLE_DEFAULT_VALUE: Text get() = Text.Resource(RCommon.string.city)

        val CITY_SEARCH_DEBOUNCE_DELAY: Duration get() = 200.milliseconds

        val MAIN_CITY_KLADR_IDS: List<KladrId>
            get() = listOf(KladrId.MOSCOW, KladrId.SAINT_PETERSBURG)
    }
}
