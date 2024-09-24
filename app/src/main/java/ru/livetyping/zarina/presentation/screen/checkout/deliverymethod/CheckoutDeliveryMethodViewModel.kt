package ru.livetyping.zarina.presentation.screen.checkout.deliverymethod

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetDeliveryMethodsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class CheckoutDeliveryMethodViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutDeliveryMethodInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val params = savedStateHandle.toRoute<CheckoutGraph.DeliveryMethod>(
        typeMap = CheckoutGraph.DeliveryMethod.typeMap(),
    )

    private val cartType = params.cartType.toCartType()

    private val deliveryMethodsRequester = FlowRequester(DeliveryMethodsRequest) {
        val city = interactor.getUserCityFlow().firstOrNull()?.getOrNull() ?: City.DEFAULT
        val params = GetDeliveryMethodsFlowUseCase.Params(cartType, city.id)
        interactor.getDeliveryMethodsFlow(params)
    }

    val step: StateFlow<Int> = ImmutableStateFlow(params.step)

    val stepCount: StateFlow<Int> = ImmutableStateFlow(cartType.checkoutStepCount)

    private val deliveryMethodsResult: StateFlow<Result<List<DeliveryMethod>>?> =
        deliveryMethodsRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val state: StateFlow<State> = combine(
        deliveryMethodsResult,
        deliveryMethodsRequester.loadingState,
    ) { result, loadingState ->
        if (result == null || loadingState.isLoading()) {
            State.Loading
        } else {
            result.fold(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        State.DeliveryMethods(it)
                    } else {
                        State.Error(ErrorState.GENERIC)
                    }
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    State.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = State.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutDeliveryMethodScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutDeliveryMethodScreenAction.CheckoutClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeliveryMethodClicked(method: DeliveryMethod) {
        navigationThrottler.throttle {
            val action = CheckoutDeliveryMethodScreenAction.DeliveryMethodSelected(
                cartType = cartType,
                step = step.value + 1,
                method = method,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeliveryMethodsErrorRefreshClicked() {
        deliveryMethodsRequester.request(DeliveryMethodsRequest)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutDeliveryMethodScreenAction) : SideEffect
    }

    @Stable
    sealed class State {
        data object Loading : State()

        @Immutable
        data class DeliveryMethods(val methods: List<DeliveryMethod>) : State()

        @Immutable
        data class Error(val state: ErrorState) : State()
    }

    private data object DeliveryMethodsRequest : FlowRequester.Request
}
