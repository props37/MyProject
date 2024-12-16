package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
internal class OrderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<OrderSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderScreenAction.BackClicked
            emitSideEffect(OrderSideEffect.Navigate(action))
        }
    }
}
