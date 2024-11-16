package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavEntry
import javax.inject.Inject

@HiltViewModel
internal class CitySelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<CitySelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<CitySelectorNavEntry>(
        typeMap = CitySelectorNavEntry.typeMap(),
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CitySelectorScreenAction.ScreenClosed
            emitSideEffect(CitySelectorSideEffect.Navigate(action))
        }
    }
}
