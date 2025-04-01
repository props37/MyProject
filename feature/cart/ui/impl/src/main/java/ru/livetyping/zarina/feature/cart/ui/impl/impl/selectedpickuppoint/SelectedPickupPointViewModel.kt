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
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupPointFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointStateBuilder
import javax.inject.Inject

@HiltViewModel
internal class SelectedPickupPointViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPickupPointFlow: GetPickupPointFlowUseCase,
) : ViewModel(), SideEffectSource<SelectedPickupPointSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<SelectedPickupPointNavEntry>(
        typeMap = SelectedPickupPointNavEntry.typeMap(),
    )

    private val pickupPointRequester = FlowRequester(PickupPointRequest) {
        val params = GetPickupPointFlowUseCase.Params(navEntry.getPickupPointId())
        getPickupPointFlow(params)
    }

    private val selectedDeliveryTypeId =
        MutableStateFlow<PickupPointDetailed.DeliveryType.Id?>(null)

    private val selectedPickupPointStateBuilder = SelectedPickupPointStateBuilder()
    val selectedPickupPointState: StateFlow<SelectedPickupPointState> = combine(
        pickupPointRequester.flow,
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

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SelectedPickupPointScreenAction.BackClicked
            emitSideEffect(SelectedPickupPointSideEffect.Navigate(action))
        }
    }

    private data object PickupPointRequest : FlowRequest
}
