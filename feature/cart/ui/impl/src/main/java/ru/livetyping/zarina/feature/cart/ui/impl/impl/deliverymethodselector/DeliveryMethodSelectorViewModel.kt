package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class DeliveryMethodSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<DeliveryMethodSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<DeliveryMethodSelectorNavEntry>(
        typeMap = DeliveryMethodSelectorNavEntry.typeMap(),
    )
    private val cartType = navEntry.cartType.toCartType()

    val checkoutStep: StateFlow<Int> = ReadOnlyStateFlow(navEntry.checkoutStep)

    val checkoutStepCount: StateFlow<Int> = ReadOnlyStateFlow(cartType.checkoutStepCount)

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = DeliveryMethodSelectorScreenAction.BackClicked
            emitSideEffect(DeliveryMethodSelectorSideEffect.Navigate(action))
        }
    }
}
