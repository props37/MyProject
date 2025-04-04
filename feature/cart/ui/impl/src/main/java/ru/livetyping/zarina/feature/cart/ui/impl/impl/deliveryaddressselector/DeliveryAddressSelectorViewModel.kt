package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.component.AddressComponent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class DeliveryAddressSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    deps: DeliveryAddressSelectorDependencies,
) : ViewModel(), SideEffectSource<DeliveryAddressSelectorSideEffect> by SideEffectSourceImpl() {

    private val addressComponent = AddressComponent(
        savedStateHandle = savedStateHandle,
        coroutineScope = viewModelScope,
        getUserCityFlowUseCase = deps.getUserCityFlow,
        getCityStreetsFlowUseCase = deps.getCityStreetsFlow,
        getStreetBuildingsFlowUseCase = deps.getStreetBuildingsFlow,
    )

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<DeliveryAddressSelectorNavEntry>(
        typeMap = DeliveryAddressSelectorNavEntry.typeMap(),
    )

    private val deliveryType = getDeliveryType(navEntry.deliveryMethod.toDeliveryMethod())

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        value = CheckoutTopBarState(
            title = Text.Resource(R.string.cart_courier_delivery),
            checkoutStep = navEntry.checkoutStep,
            checkoutStepCount = navEntry.cartType.toCartType().checkoutStepCount,
            isBackButtonVisible = true,
        )
    )

    private val initialDeliveryAddressSelectorState = DeliveryAddressSelectorState(
        deliveryType = deliveryType,
        city = null,
        streetSelectorTextFieldState = addressComponent.streetSelectorTextFieldState,
        buildingSelectorTextFieldState = addressComponent.buildingSelectorTextFieldState,
        apartmentSelectorTextFieldState = addressComponent.apartmentSelectorTextFieldState,
        isBuildingSelectionEnabled = false,
    )

    private val getUserCityUseCaseParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
    private val cityFlow = deps.getUserCityFlow(getUserCityUseCaseParams)
        .map { it.getOrNull() }

    val deliveryAddressSelectorState: StateFlow<DeliveryAddressSelectorState> = combine(
        cityFlow,
        addressComponent.isBuildingSelectionEnabled,
    ) { city, isBuildingSelectionEnabled ->
        DeliveryAddressSelectorState(
            deliveryType = deliveryType,
            city = city,
            streetSelectorTextFieldState = addressComponent.streetSelectorTextFieldState,
            buildingSelectorTextFieldState = addressComponent.buildingSelectorTextFieldState,
            apartmentSelectorTextFieldState = addressComponent.apartmentSelectorTextFieldState,
            isBuildingSelectionEnabled = isBuildingSelectionEnabled,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = initialDeliveryAddressSelectorState,
    )

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
        }
    }

    // TODO: [Top] Implement
    fun onDeliveryAddressSelectorEvent(event: DeliveryAddressSelectorEvent) {
        when (event) {
            DeliveryAddressSelectorEvent.StreetSelectorClicked -> TODO()
            DeliveryAddressSelectorEvent.BuildingSelectorClicked -> TODO()
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

    private fun getDeliveryType(deliveryMethod: DeliveryMethod): DeliveryType {
        return when (deliveryMethod.type) {
            DeliveryMethodType.COURIER_EXPRESS -> DeliveryType.COURIER
            DeliveryMethodType.POST -> DeliveryType.POST
            else -> error("Delivery method $deliveryMethod is not supported")
        }
    }

    private companion object {
        private const val TAG = "DeliveryAddressSelectorViewModel"
    }
}
