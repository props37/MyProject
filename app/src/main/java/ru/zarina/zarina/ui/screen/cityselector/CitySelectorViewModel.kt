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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.geography.GetCitiesUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CitySelectorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: CitySelectorInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val cityNameQuery: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_CITY_NAME_QUERY,
        initialValue = "",
    )

    // TODO: [High] Refactor
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val cityListState = cityNameQuery
        .debounce(300.milliseconds) // TODO: [High] Is debounce needed?
        .flatMapLatest { nameQuery ->
            val getCitiesParams = GetCitiesUseCase.Params(nameQuery)
            interactor.getCities(getCitiesParams).map { result ->
                result.fold(
                    onSuccess = { cities ->
                        val listItems = cities.map {
                            CityListItem.City(it)
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

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val result = CitySelectorScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: CitySelectorScreenResult) : SideEffect
    }

    sealed class CityListState {
        data object InitialLoading : CityListItem()

        data class CityList(val list: List<CityListItem>) : CityListState()

        data class Error(val throwable: Throwable) : CityListState()
    }

    sealed class CityListItem {
        data class City(val city: ru.zarina.zarina.domain.rework.geography.City) : CityListItem()

        data class CityFirstLetterHeader(val letter: Char) : CityListItem()
    }

    companion object {
        private const val KEY_CITY_NAME_QUERY = "city_name_query"
    }
}

