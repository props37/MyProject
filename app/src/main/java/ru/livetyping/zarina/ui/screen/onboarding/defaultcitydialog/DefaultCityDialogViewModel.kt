package ru.livetyping.zarina.ui.screen.onboarding.defaultcitydialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.onboarding.defaultcitydialog.DefaultCityDialogViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class DefaultCityDialogViewModel @Inject constructor() : ViewModel(),
    SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val defaultCity = MutableStateFlow(City.DEFAULT).asStateFlow()

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = DefaultCityDialogScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: DefaultCityDialogScreenAction) : SideEffect
    }
}
