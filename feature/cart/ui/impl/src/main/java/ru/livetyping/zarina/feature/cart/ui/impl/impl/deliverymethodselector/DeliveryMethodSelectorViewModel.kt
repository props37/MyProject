package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.usecase.checkout.GetDeliveryMethodsFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.model.DeliveryMethodSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.model.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.model.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class DeliveryMethodSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: DeliveryMethodSelectorDependencies,
) : ViewModel(), SideEffectSource<DeliveryMethodSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<DeliveryMethodSelectorNavEntry>(
        typeMap = DeliveryMethodSelectorNavEntry.typeMap(),
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

    val checkoutStepCount: StateFlow<Int> = ReadOnlyStateFlow(cartType.checkoutStepCount)

    private val deliveryMethodsRequester = FlowRequester(DeliveryMethodsRequest, viewModelScope) {
        val params = GetDeliveryMethodsFlowUseCase.Params(cartType)
        deps.getDeliveryMethodsFlow(params)
    }

    val deliveryMethodSelectorState: StateFlow<DeliveryMethodSelectorState> = combine(
        deliveryMethodsRequester.flow,
        deliveryMethodsRequester.loadingState,
    ) { result, loadingState ->
        if (loadingState.isLoading()) {
            DeliveryMethodSelectorState.Loading
        } else {
            result.fold(
                onSuccess = { deliveryMethods ->
                    if (deliveryMethods.isNotEmpty()) {
                        DeliveryMethodSelectorState.Success(deliveryMethods.toImmutableList())
                    } else {
                        DeliveryMethodSelectorState.Error(ZarinaErrorScreenState.GENERIC)
                    }
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    DeliveryMethodSelectorState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = DeliveryMethodSelectorState.Loading,
    )

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
        }
    }

    fun onDeliveryMethodClicked(deliveryMethod: DeliveryMethod) {
        navigationThrottler.throttle {
            // TODO: [Top] Report AppMetrica event
            val action = DeliveryMethodSelectorScreenAction.DeliveryMethodSelected(
                cartType = cartType,
                currentCheckoutStep = checkoutStep,
                recipient = navEntry.recipient.toRecipient(),
                deliveryMethod = deliveryMethod,
            )
            emitSideEffect(DeliveryMethodSelectorSideEffect.Navigate(action))
        }
    }

    fun onDeliveryMethodsErrorRefreshClicked() {
        deliveryMethodsRequester.request(DeliveryMethodsRequest)
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = DeliveryMethodSelectorScreenAction.BackClicked
            emitSideEffect(DeliveryMethodSelectorSideEffect.Navigate(action))
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = DeliveryMethodSelectorScreenAction.CloseClicked
            emitSideEffect(DeliveryMethodSelectorSideEffect.Navigate(action))
        }
    }

    private data object DeliveryMethodsRequest : FlowRequest
}
