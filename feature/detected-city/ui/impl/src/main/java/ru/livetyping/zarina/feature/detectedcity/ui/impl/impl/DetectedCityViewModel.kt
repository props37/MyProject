package ru.livetyping.zarina.feature.detectedcity.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityNavEntry
import javax.inject.Inject

@HiltViewModel
internal class DetectedCityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<DetectedCitySideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<DetectedCityNavEntry>()

    val cityName: StateFlow<String> = ReadOnlyStateFlow(navEntry.cityName)

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = DetectedCityScreenAction.CloseClicked
            emitSideEffect(DetectedCitySideEffect.Navigate(action))
        }
    }
}
