package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
internal class PickupStoreSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<PickupStoreSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<PickupStoreSelectorNavEntry>(
        typeMap = PickupStoreSelectorNavEntry.typeMap(),
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PickupStoreSelectorScreenAction.BackClicked
            emitSideEffect(PickupStoreSelectorSideEffect.Navigate(action))
        }
    }
}
