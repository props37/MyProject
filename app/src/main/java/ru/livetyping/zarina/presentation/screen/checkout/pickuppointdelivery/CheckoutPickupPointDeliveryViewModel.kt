package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetPickupPointsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutPickupPointDeliveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutPickupPointDeliveryInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType: StateFlow<CartType> = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.PickupPointDelivery.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "cartType is null" }
            it.toCartType()
        }

    val step: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CheckoutGraph.PickupPointDelivery.ARG_KEY_STEP,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "step is null" }
        }

    private val deliveryMethodType: StateFlow<DeliveryMethodType> = savedStateHandle
        .getStateFlow<DeliveryMethodTypeParcelable?>(
            key = CheckoutGraph.PickupPointDelivery.ARG_DELIVERY_METHOD_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "deliveryMethodType is null" }
            it.toDeliveryMethodType()
        }

    val stepCount: StateFlow<Int> = MutableStateFlow(cartType.value.checkoutStepCount).asStateFlow()

    val viewModes: StateFlow<List<ViewMode>> = MutableStateFlow(ViewMode.entries).asStateFlow()

    private val _currentViewMode = MutableStateFlow(ViewMode.MAP)
    val currentViewMode: StateFlow<ViewMode> = _currentViewMode.asStateFlow()

    private val city: Flow<City?> = interactor.getUserCityFlow().map {
        it.getOrDefault(City.DEFAULT)
    }

    @Suppress("NAME_SHADOWING")
    @OptIn(ExperimentalCoroutinesApi::class)
    private val pickupPointsRequester = FlowRequester(PickupPointsRequest) {
        city.flatMapLatest { city ->
            val city = city ?: City.DEFAULT
            val params = GetPickupPointsFlowUseCase.Params(city.id)
            interactor.getPickupPointsFlow(params)
        }
    }

    private val pickupPointsResult: StateFlow<Result<List<PickupPoint>>?> =
        pickupPointsRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val pickupPointsState: StateFlow<PickupPointsState> = combine(
        pickupPointsResult,
        pickupPointsRequester.loadingState,
    ) { result, loadingState ->
        createPickupPointsState(result, loadingState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = PickupPointsState.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutPickupPointDeliveryScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutPickupPointDeliveryScreenAction.CheckoutClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onViewModeChanged(mode: ViewMode) {
        _currentViewMode.value = mode
    }

    private fun createPickupPointsState(
        pickupPointsResult: Result<List<PickupPoint>>?,
        loadingState: FlowRequester.LoadingState,
    ): PickupPointsState {
        return if (pickupPointsResult == null || loadingState.isLoading()) {
            PickupPointsState.Loading
        } else {
            pickupPointsResult.fold(
                onSuccess = { pickupPoints ->
                    PickupPointsState.Success(pickupPoints)
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    PickupPointsState.Error(errorState)
                },
            )
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutPickupPointDeliveryScreenAction) : SideEffect
    }

    enum class ViewMode { MAP, LIST }

    @Stable
    sealed class PickupPointsState {
        @Immutable
        data class Success(val pickupPoints: List<PickupPoint>) : PickupPointsState()

        data object Loading : PickupPointsState()

        @Immutable
        data class Error(val state: ErrorState) : PickupPointsState()
    }

    private data object PickupPointsRequest : FlowRequester.Request
}
