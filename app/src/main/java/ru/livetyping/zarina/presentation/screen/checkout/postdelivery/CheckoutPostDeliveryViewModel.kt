package ru.livetyping.zarina.presentation.screen.checkout.postdelivery

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.DeliveryOptions
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.common.DeliveryOptionsState
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressViewModelComponent
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetPostDeliveryOptionsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutPostDeliveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    interactor: CheckoutPostDeliveryInteractor,
    private val addressComponent: CheckoutAddressViewModelComponent,
) : ViewModel(addressComponent), SideEffectSource<CheckoutPostDeliveryViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType: StateFlow<CartType> = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.PostDelivery.ARG_KEY_CART_TYPE,
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
            key = CheckoutGraph.PostDelivery.ARG_KEY_STEP,
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
            key = CheckoutGraph.PostDelivery.ARG_DELIVERY_METHOD_TYPE,
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

    val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { it.getOrDefault(City.DEFAULT) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val isBuildingSelectionEnabled: StateFlow<Boolean> = addressComponent.isBuildingSelectionEnabled
    val streetTextFieldState: TextFieldState = addressComponent.streetTextFieldState
    val buildingTextFieldState: TextFieldState = addressComponent.buildingTextFieldState
    val apartmentTextFieldState: TextFieldState = addressComponent.apartmentTextFieldState
    val searchStreetTextFieldState: TextFieldState = addressComponent.searchStreetTextFieldState
    val searchBuildingTextFieldState: TextFieldState = addressComponent.searchBuildingTextFieldState
    val searchApartmentTextFieldState: TextFieldState =
        addressComponent.searchApartmentTextFieldState

    val streetsState: StateFlow<CheckoutAddressViewModelComponent.State> =
        addressComponent.streetsState
    val buildingsState: StateFlow<CheckoutAddressViewModelComponent.State> =
        addressComponent.buildingsState

    @OptIn(ExperimentalCoroutinesApi::class)
    private val deliveryOptionsRequester = FlowRequester(DeliveryOptionsRequest) {
        addressComponent.selectedBuilding.flatMapLatest { building ->
            if (building != null) {
                markAsLoading(it)
                val params = GetPostDeliveryOptionsFlowUseCase.Params(building.id)
                interactor.getPostDeliveryOptionsFlow(params)
            } else flowOf(null)
        }
    }

    private val deliveryOptionsResult: StateFlow<Result<DeliveryOptions>?> =
        deliveryOptionsRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    private val selectedDeliveryOptionId = MutableStateFlow<DeliveryOptions.Option.Id?>(null)

    val deliveryOptionsState: StateFlow<DeliveryOptionsState?> = combine(
        deliveryOptionsResult,
        deliveryOptionsRequester.loadingState,
        selectedDeliveryOptionId,
    ) { optionsResult, loadingState, selectedOptionId ->
        when {
            loadingState.isLoading() -> DeliveryOptionsState.Loading
            optionsResult != null -> {
                DeliveryOptionsState.create(
                    optionsResult = optionsResult,
                    loadingState = loadingState,
                    selectedOptionId = selectedOptionId,
                    deliveryOptionToSelectedDateTimePeriod = emptyMap(),
                    getOptionDefaultDateTimePeriod = { it.dateTimePeriods.first() },
                )
            }

            else -> null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = null,
    )

    val isContinueButtonVisible: StateFlow<Boolean> = deliveryOptionsState.mapState(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
    ) { state ->
        state?.let { it.findSelectedOption() != null } ?: false
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutPostDeliveryScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutPostDeliveryScreenAction.CheckoutClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onStreetSelected(street: CheckoutAddressViewModelComponent.Item) {
        addressComponent.onStreetSelected(street)
    }

    fun onBuildingSelected(building: CheckoutAddressViewModelComponent.Item) {
        addressComponent.onBuildingSelected(building)
    }

    fun onStreetsErrorRefreshClicked() {
        addressComponent.onStreetsErrorRefreshClicked()
    }

    fun onBuildingsErrorRefreshClicked() {
        addressComponent.onBuildingsErrorRefreshClicked()
    }

    fun onDeliveryOptionClicked(option: DeliveryOptions.Option) {
        selectedDeliveryOptionId.value = option.id
    }

    fun onDeliveryOptionsErrorRefreshClicked() {
        deliveryOptionsRequester.request(DeliveryOptionsRequest)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutPostDeliveryScreenAction) : SideEffect
    }

    private data object DeliveryOptionsRequest : FlowRequester.Request
}
