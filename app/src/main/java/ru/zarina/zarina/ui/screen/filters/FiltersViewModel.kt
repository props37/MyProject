package ru.zarina.zarina.ui.screen.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val interactor: FiltersInteractor,
) : ViewModel(), SideEffectSource<FiltersViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward(FiltersScreenResult.ScreenClosed))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: FiltersScreenResult) : SideEffect
    }
}
