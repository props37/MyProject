package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupPointFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointStateBuilder
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class SelectedPickupPointViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPickupPointFlow: GetPickupPointFlowUseCase,
    private val getUserCityFlow: GetUserCityFlowUseCase,
) : ViewModel(), SideEffectSource<SelectedPickupPointSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<SelectedPickupPointNavEntry>(
        typeMap = SelectedPickupPointNavEntry.typeMap(),
    )

    private val pickupPointRequester = FlowRequester(PickupPointRequest) {
        val params = GetPickupPointFlowUseCase.Params(navEntry.getPickupPointId())
        getPickupPointFlow(params)
    }

    private val pickupPointResultFlow = pickupPointRequester.flow.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    private val selectedDeliveryTypeId =
        MutableStateFlow<PickupPointDetailed.DeliveryType.Id?>(null)

    private val selectedPickupPointStateBuilder = SelectedPickupPointStateBuilder()
    val selectedPickupPointState: StateFlow<SelectedPickupPointState> = combine(
        pickupPointResultFlow,
        pickupPointRequester.loadingState,
        selectedDeliveryTypeId,
    ) { result, loadingState, selectedDeliveryTypeId ->
        selectedPickupPointStateBuilder.build(
            pickupPointResult = result,
            pickupPointLoadingState = loadingState,
            selectedDeliveryTypeId = selectedDeliveryTypeId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SelectedPickupPointState.Loading,
    )

    fun onSelectedPickupPointEvent(event: SelectedPickupPointEvent) {
        when (event) {
            SelectedPickupPointEvent.BackClicked -> onBackClicked()
            SelectedPickupPointEvent.ContinueClicked -> onContinueClicked()
            is SelectedPickupPointEvent.DeliveryTypeClicked -> onDeliveryTypeClicked(event)
            SelectedPickupPointEvent.ErrorRefreshClicked -> onErrorRefreshClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SelectedPickupPointScreenAction.BackClicked
            emitSideEffect(SelectedPickupPointSideEffect.Navigate(action))
        }
    }

    private fun onContinueClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val pickupPoint = pickupPointResultFlow.firstOrNull()?.getOrNull()
                val selectedDeliveryType = pickupPoint?.deliveryTypes?.find { deliveryType ->
                    deliveryType.id == selectedDeliveryTypeId.value
                } ?: pickupPoint?.deliveryTypes?.firstOrNull()
                val city = getUserCityFlow(GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly))
                    .firstOrNull()?.getOrNull()

                if (pickupPoint != null && selectedDeliveryType != null && city != null) {
                    val checkoutParams = PickupFromPickupPointCheckoutParams(
                        cartType = navEntry.cartType.toCartType(),
                        deliveryMethod = navEntry.deliveryMethod.toDeliveryMethod(),
                        recipient = navEntry.recipient.toRecipient(),
                        city = city,
                        pickupPoint = pickupPoint,
                        deliveryType = selectedDeliveryType,
                        dateTimePeriod = selectedDeliveryType.dateTimePeriods.first(),
                    )
                    val action = SelectedPickupPointScreenAction.ContinueClicked(
                        currentCheckoutStep = navEntry.checkoutStep,
                        checkoutParams = checkoutParams,
                    )
                    emitSideEffect(SelectedPickupPointSideEffect.Navigate(action))
                } else {
                    val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
                    val message = ZarinaToastMessage.error(messageText)
                    emitSideEffect(SelectedPickupPointSideEffect.ShowZarinaToast(message))
                }
            }
        }
    }

    private fun onDeliveryTypeClicked(event: SelectedPickupPointEvent.DeliveryTypeClicked) {
        selectedDeliveryTypeId.value = event.deliveryType.id
    }

    private fun onErrorRefreshClicked() {
        pickupPointRequester.request(PickupPointRequest)
    }

    private data object PickupPointRequest : FlowRequest
}
