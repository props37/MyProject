package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class DeliveryAddressSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<DeliveryAddressSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<DeliveryAddressSelectorNavEntry>(
        typeMap = DeliveryAddressSelectorNavEntry.typeMap(),
    )

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        value = CheckoutTopBarState(
            checkoutStep = navEntry.checkoutStep,
            checkoutStepCount = navEntry.cartType.toCartType().checkoutStepCount,
            isBackButtonVisible = true,
        )
    )

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = DeliveryAddressSelectorScreenAction.BackClicked
            emitSideEffect(DeliveryAddressSelectorSideEffect.Navigate(action))
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = DeliveryAddressSelectorScreenAction.CloseClicked
            emitSideEffect(DeliveryAddressSelectorSideEffect.Navigate(action))
        }
    }
}
