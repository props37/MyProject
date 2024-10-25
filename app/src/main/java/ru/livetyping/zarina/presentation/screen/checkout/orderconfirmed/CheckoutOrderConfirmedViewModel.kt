package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class CheckoutOrderConfirmedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val orderConfirmed = savedStateHandle.toRoute<CheckoutGraph.OrderConfirmed>(
        typeMap = CheckoutGraph.OrderConfirmed.typeMap(),
    )

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutOrderConfirmedScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutOrderConfirmedScreenAction) : SideEffect
    }
}
