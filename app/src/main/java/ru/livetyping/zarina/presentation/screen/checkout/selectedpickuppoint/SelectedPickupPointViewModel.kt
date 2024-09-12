package ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetPickupPointDetailsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class SelectedPickupPointViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SelectedPickupPointInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType: StateFlow<CartType> = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.SelectedPickupPoint.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "cartType is null" }
            it.toCartType()
        }

    private val step: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CheckoutGraph.SelectedPickupPoint.ARG_KEY_STEP,
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
            key = CheckoutGraph.SelectedPickupPoint.ARG_DELIVERY_METHOD_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "deliveryMethodType is null" }
            it.toDeliveryMethodType()
        }

    private val pickupPointId: StateFlow<PickupPoint.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = CheckoutGraph.SelectedPickupPoint.ARG_PICKUP_POINT_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "pickupPointId is null" }
            PickupPoint.Id(it)
        }

    private val city: Flow<City?> = interactor.getUserCityFlow().map {
        it.getOrDefault(City.DEFAULT)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Suppress("NAME_SHADOWING")
    private val pickupPointRequester = FlowRequester(PickupPointRequest) {
        combine(city, pickupPointId) { city, pickupPointId ->
            val city = city ?: City.DEFAULT
            val params = GetPickupPointDetailsFlowUseCase.Params(city.id, pickupPointId)
            interactor.getPickupPointDetailsFlow(params)
        }
            .flatMapLatest { it }
    }

    private val pickupPointResult: StateFlow<Result<PickupPointDetails>?> = pickupPointRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val selectedDeliveryTypeId =
        MutableStateFlow<PickupPointDetails.DeliveryType.Id?>(null)

    val pickupPointState: StateFlow<PickupPointState> = combine(
        pickupPointResult,
        pickupPointRequester.loadingState,
        selectedDeliveryTypeId,
    ) { result, loadingState, selectedDeliveryTypeId ->
        createPickupPointState(
            pickupPointResult = result,
            loadingState = loadingState,
            selectedDeliveryTypeId = selectedDeliveryTypeId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = PickupPointState.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SelectedPickupPointScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onPickupPointErrorRefreshClicked() {
        pickupPointRequester.request(PickupPointRequest)
    }

    fun onPickupPointDeliveryTypeClicked(type: PickupPointDetails.DeliveryType) {
        selectedDeliveryTypeId.value = type.id
    }

    private fun List<PickupPointDetails.DeliveryType>.getDefault(): PickupPointDetails.DeliveryType {
        return this.first()
    }

    private fun createPickupPointState(
        pickupPointResult: Result<PickupPointDetails>?,
        loadingState: FlowRequester.LoadingState,
        selectedDeliveryTypeId: PickupPointDetails.DeliveryType.Id?,
    ): PickupPointState {
        return if (pickupPointResult == null || loadingState.isLoading()) {
            PickupPointState.Loading
        } else {
            pickupPointResult.fold(
                onSuccess = { pickupPoint ->
                    @Suppress("NAME_SHADOWING")
                    val selectedDeliveryTypeId =
                        selectedDeliveryTypeId ?: pickupPoint.deliveryTypes.getDefault().id
                    PickupPointState.Success(
                        pickupPoint = pickupPoint,
                        selectedDeliveryTypeId = selectedDeliveryTypeId,
                    )
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    PickupPointState.Error(errorState)
                }
            )
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SelectedPickupPointScreenAction) : SideEffect
    }

    @Stable
    sealed class PickupPointState {
        @Immutable
        data class Success(
            val pickupPoint: PickupPointDetails,
            val selectedDeliveryTypeId: PickupPointDetails.DeliveryType.Id,
        ) : PickupPointState()

        data object Loading : PickupPointState()

        @Immutable
        data class Error(val state: ErrorState) : PickupPointState()
    }

    private data object PickupPointRequest : FlowRequester.Request
}
