package ru.zarina.zarina.ui.screen.defaultcitydialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class DefaultCityDialogViewModel @Inject constructor() : ViewModel(),
    SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val defaultCity = MutableStateFlow(City.SAINT_PETERSBURG).asStateFlow()

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val result = DefaultCityDialogScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: DefaultCityDialogScreenResult) : SideEffect
    }
}
