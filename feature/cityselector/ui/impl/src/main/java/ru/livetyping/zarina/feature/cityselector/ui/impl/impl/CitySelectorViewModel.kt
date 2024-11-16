package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavEntry
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class CitySelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<CitySelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<CitySelectorNavEntry>(
        typeMap = CitySelectorNavEntry.typeMap(),
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val citySearchTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val selectedCityValueHolder = savedStateHandle.createValueHolder<CityParcelable?>(
        key = Keys.SELECTED_CITY.key,
        initialValue = navEntry.currentCity,
    )

    private val hasSelectedCityChanged = MutableStateFlow(false)

    val citySelectorState: StateFlow<CitySelectorState> = combine(
        selectedCityValueHolder.stateFlow,
        hasSelectedCityChanged,
    ) { selectedCity, hasSelectedCityChanged ->
        CitySelectorState(
            title = navEntry.title ?: TITLE_DEFAULT_VALUE,
            citySearchTextFieldState = citySearchTextFieldState,
            selectedCity = selectedCity?.toCity(),
            isChangeCityButtonVisible = hasSelectedCityChanged,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CitySelectorState(
            title = navEntry.title ?: TITLE_DEFAULT_VALUE,
            citySearchTextFieldState = citySearchTextFieldState,
            selectedCity = selectedCityValueHolder.get()?.toCity(),
            isChangeCityButtonVisible = false,
        )
    )

    fun onCitySelectorEvent(event: CitySelectorEvent) {
        when (event) {
            CitySelectorEvent.BackClicked -> onBackClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CitySelectorScreenAction.ScreenClosed
            emitSideEffect(CitySelectorSideEffect.Navigate(action))
        }
    }

    private enum class Keys {
        SELECTED_CITY;

        val key: String get() = name
    }

    private companion object {
        val TITLE_DEFAULT_VALUE: Text get() = Text.Resource(RCommon.string.city)
    }
}
