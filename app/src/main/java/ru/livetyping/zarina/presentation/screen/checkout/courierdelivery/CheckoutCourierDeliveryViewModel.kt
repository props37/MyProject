package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.DeliveryOptions
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.common.DeliveryOptionsState
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressViewModelComponent
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetCourierDeliveryOptionsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState

@HiltViewModel(assistedFactory = CheckoutCourierDeliveryViewModel.Factory::class)
class CheckoutCourierDeliveryViewModel @AssistedInject constructor(
    @Assisted
    private val dateTimePeriodSelectorResultFlow: StateFlow<CheckoutGraph.CourierDeliveryDateTimeSelector.Result?>,
    savedStateHandle: SavedStateHandle,
    interactor: CheckoutCourierDeliveryInteractor,
    private val addressComponent: CheckoutAddressViewModelComponent,
) : ViewModel(addressComponent), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val cartType: StateFlow<CartType> = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.CourierDelivery.ARG_KEY_CART_TYPE,
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
            key = CheckoutGraph.CourierDelivery.ARG_KEY_STEP,
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
            key = CheckoutGraph.CourierDelivery.ARG_DELIVERY_METHOD_TYPE,
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
                val params = GetCourierDeliveryOptionsFlowUseCase.Params(building.id)
                interactor.getCourierDeliveryOptionsFlow(params)
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

    private val deliveryOptionToSelectedDateTimePeriod =
        MutableStateFlow<Map<DeliveryOptions.Option.Id, DeliveryOptions.Option.DateTimePeriod>>(
            emptyMap()
        )

    val deliveryOptionsState: StateFlow<DeliveryOptionsState?> = combine(
        deliveryOptionsResult,
        deliveryOptionsRequester.loadingState,
        selectedDeliveryOptionId,
        deliveryOptionToSelectedDateTimePeriod,
    ) { optionsResult, loadingState, selectedOptionId, deliveryOptionToSelectedDateTimePeriod ->
        when {
            loadingState.isLoading() -> DeliveryOptionsState.Loading
            optionsResult != null -> {
                DeliveryOptionsState.create(
                    optionsResult = optionsResult,
                    loadingState = loadingState,
                    selectedOptionId = selectedOptionId,
                    deliveryOptionToSelectedDateTimePeriod = deliveryOptionToSelectedDateTimePeriod,
                    getOptionDefaultDateTimePeriod = { it.dateTimePeriods.getDefault() },
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

    init {
        handleDateTimePeriodSelectorResult()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutCourierDeliveryScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutCourierDeliveryScreenAction.CheckoutClosed
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

    fun onDeliveryOptionDateClicked(option: DeliveryOptions.Option) {
        navigationThrottler.throttle {
            val datePeriods = option.dateTimePeriods.distinctBy { it.date }
            val action =
                CheckoutCourierDeliveryScreenAction.DeliveryDateClicked(option.id, datePeriods)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeliveryOptionTimeClicked(option: DeliveryOptions.Option) {
        navigationThrottler.throttle {
            val selectedDateTimePeriod =
                deliveryOptionToSelectedDateTimePeriod.value[option.id] ?: option.dateTimePeriods.getDefault()
            val timePeriods = option.dateTimePeriods.filter {
                it.date == selectedDateTimePeriod.date
            }
            val action =
                CheckoutCourierDeliveryScreenAction.DeliveryTimeClicked(option.id, timePeriods)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeliveryOptionsErrorRefreshClicked() {
        deliveryOptionsRequester.request(DeliveryOptionsRequest)
    }

    fun onContinueClicked() {
        val address = addressComponent.getAddress()
        val deliveryOption = deliveryOptionsState.value?.findSelectedOption()

        if (address != null && deliveryOption != null) {
            navigationThrottler.throttle {
                val cartType = cartType.value
                val checkoutParams = CourierDeliveryCheckoutParams(
                    cartType = cartType,
                    deliveryMethodType = deliveryMethodType.value,
                    address = address,
                    deliveryOptionId = deliveryOption.deliveryOption.id,
                    dateTimePeriodId = deliveryOption.selectedDateTimePeriod.id,
                )
                val action = CheckoutCourierDeliveryScreenAction.ContinueClicked(
                    cartType = cartType,
                    step = step.value + 1,
                    checkoutParams = checkoutParams,
                )
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            @Suppress("KotlinConstantConditions")
            val messageResId = when {
                address == null -> R.string.you_should_enter_address_first
                deliveryOption == null -> R.string.you_should_select_delivery_option_first
                else -> R.string.something_went_wrong
            }
            val message = ZarinaToastMessage.error(Text.Resource(messageResId))
            emitSideEffect(SideEffect.ShowZarinaToast(message))
        }
    }

    private fun handleDateTimePeriodSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<CheckoutGraph.CourierDeliveryDateTimeSelector.Result>(
                resultFlow = dateTimePeriodSelectorResultFlow,
                key = KEY_RESULT_DATE_TIME_PERIOD_SELECTOR_RESULT,
            ) { result ->
                val deliveryOptionId = DeliveryOptions.Option.Id(result.deliveryOptionId)
                deliveryOptionToSelectedDateTimePeriod.update {
                    it + (deliveryOptionId to result.dateTimePeriod.toDateTimePeriod())
                }
            }
        }
    }

    @Suppress("MaxLineLength")
    private fun List<DeliveryOptions.Option.DateTimePeriod>.getDefault(): DeliveryOptions.Option.DateTimePeriod {
        return this.first()
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutCourierDeliveryScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private data object DeliveryOptionsRequest : FlowRequester.Request

    @AssistedFactory
    interface Factory {
        fun create(
            dateTimePeriodSelectorResultFlow: StateFlow<CheckoutGraph.CourierDeliveryDateTimeSelector.Result?>,
        ): CheckoutCourierDeliveryViewModel
    }

    companion object {
        private const val KEY_RESULT_DATE_TIME_PERIOD_SELECTOR_RESULT = "result_date_time_period_selector"
    }
}
