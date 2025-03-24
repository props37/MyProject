package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class PickupStoreSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: PickupStoreSelectorDependencies,
) : ViewModel(), SideEffectSource<PickupStoreSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<PickupStoreSelectorNavEntry>(
        typeMap = PickupStoreSelectorNavEntry.typeMap(),
    )
    private val cartType = navEntry.cartType.toCartType()
    private val checkoutStep = navEntry.checkoutStep

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        CheckoutTopBarState(
            checkoutStep = checkoutStep,
            checkoutStepCount = cartType.checkoutStepCount,
            isBackButtonVisible = true,
        )
    )

    private val cartRequester = FlowRequester(CartRequest) {
        val params = GetCartFlowUseCase.Params(cartType)
        deps.getCartFlow(params)
    }

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> TODO() // TODO: [Top] Implement
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PickupStoreSelectorScreenAction.BackClicked
            emitSideEffect(PickupStoreSelectorSideEffect.Navigate(action))
        }
    }

    private data object CartRequest : FlowRequest
}
