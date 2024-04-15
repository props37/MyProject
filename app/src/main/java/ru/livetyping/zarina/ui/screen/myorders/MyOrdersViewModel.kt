package ru.livetyping.zarina.ui.screen.myorders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.myorders.MyOrdersViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MyOrdersInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = MyOrdersScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: MyOrdersScreenAction) : SideEffect
    }
}
