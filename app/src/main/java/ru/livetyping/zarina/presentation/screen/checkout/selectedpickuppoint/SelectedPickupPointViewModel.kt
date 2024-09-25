package ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetPickupPointDetailsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class SelectedPickupPointViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SelectedPickupPointInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val params = savedStateHandle.toRoute<CheckoutGraph.SelectedPickupPoint>(
        typeMap = CheckoutGraph.SelectedPickupPoint.typeMap(),
    )

    private val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { it.getOrDefault(City.DEFAULT) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val pickupPointRequester = FlowRequester(PickupPointRequest) {
        city.flatMapLatest {
            val city = it ?: City.DEFAULT
            val pickupPointId = PickupPoint.Id(params.pickupPointId)
            val params = GetPickupPointDetailsFlowUseCase.Params(city.id, pickupPointId)
            interactor.getPickupPointDetailsFlow(params)
        }
    }

    private var pickupPoint: PickupPointDetails? = null

    private val pickupPointResult: StateFlow<Result<PickupPointDetails>?> = pickupPointRequester.flow
        .onEach { result ->
            val pickupPoint = result.getOrNull()
            if (pickupPoint != null) {
                this.pickupPoint = pickupPoint
            }
        }
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

    fun onContinueClicked() {
        val successPickupPointState = pickupPointState.value as? PickupPointState.Success
        val pickupPoint = successPickupPointState?.pickupPoint
        val selectedDeliveryType = successPickupPointState?.let { state ->
            state.pickupPoint.deliveryTypes
                .find { it.id == state.selectedDeliveryTypeId }
                ?: state.pickupPoint.deliveryTypes.getDefault()
        }

        if (pickupPoint != null && selectedDeliveryType != null) {
            navigationThrottler.throttle {
                val checkoutParams = PickupPointDeliveryCheckoutParams(
                    cartType = params.cartType.toCartType(),
                    deliveryMethodType = params.deliveryMethodType.toDeliveryMethodType(),
                    city = city.value ?: City.DEFAULT,
                    pickupPoint = pickupPoint,
                    deliveryType = selectedDeliveryType,
                    dateTimePeriod = selectedDeliveryType.dateTimePeriods.first(),
                    customer = params.customer.toCustomer(),
                )
                val action = SelectedPickupPointScreenAction.ContinueClicked(
                    step = params.step + 1,
                    checkoutParams = checkoutParams,
                )
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            val messageResId = R.string.something_went_wrong
            val message = ZarinaToastMessage.error(Text.Resource(messageResId))
            emitSideEffect(SideEffect.ShowZarinaToast(message))
        }
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

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
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
