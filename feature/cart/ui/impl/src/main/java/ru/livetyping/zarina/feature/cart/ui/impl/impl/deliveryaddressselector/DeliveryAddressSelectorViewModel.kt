package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.domain.model.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.usecase.checkout.GetCourierDeliveryOptionsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPostDeliveryOptionsFlowUseCase
import ru.livetyping.zarina.core.navigationutil.ScreenResultHandler
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.component.AddressComponent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryOptionDateTimeType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryOptionsState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.GenericBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorScreenResult
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel(assistedFactory = DeliveryAddressSelectorViewModel.Factory::class)
internal class DeliveryAddressSelectorViewModel @AssistedInject constructor(
    @Assisted
    private val deliveryOptionDateTimeSelectorResultFlow: Flow<DeliveryOptionDateTimeSelectorScreenResult?>,
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

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navEntry = savedStateHandle.toRoute<DeliveryAddressSelectorNavEntry>(
        typeMap = DeliveryAddressSelectorNavEntry.typeMap(),
    )

    private val deliveryType = getDeliveryType(navEntry.deliveryMethod.toDeliveryMethod())

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        value = run {
            val titleResId = when (deliveryType) {
                DeliveryType.COURIER -> R.string.cart_courier_delivery
                DeliveryType.POST -> R.string.cart_post_delivery
            }
            CheckoutTopBarState(
                title = Text.Resource(titleResId),
                checkoutStep = navEntry.checkoutStep,
                checkoutStepCount = navEntry.cartType.toCartType().checkoutStepCount,
                isBackButtonVisible = true,
            )
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val deliveryOptionsRequester = FlowRequester(DeliveryOptionsRequest) {
        addressComponent.currentAddress
            .distinctUntilChangedBy { it?.building }
            .flatMapLatest { address ->
                if (address?.building != null) {
                    markAsLoading(DeliveryOptionsRequest)
                    when (deliveryType) {
                        DeliveryType.COURIER -> {
                            val params =
                                GetCourierDeliveryOptionsFlowUseCase.Params(address.building.id)
                            deps.getCourierDeliveryOptionsFlow(params)
                        }

                        DeliveryType.POST -> {
                            val params =
                                GetPostDeliveryOptionsFlowUseCase.Params(address.building.id)
                            deps.getPostDeliveryOptionsFlow(params)
                        }
                    }
                } else {
                    flowOf(null)
                }
            }
    }

    private val deliveryOptionsResultFlow = deliveryOptionsRequester.flow.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    private val selectedDeliveryOptionId = MutableStateFlow<DeliveryOption.Id?>(null)

    private val deliveryOptionToSelectedDateTimePeriod =
        MutableStateFlow<Map<DeliveryOption.Id, DeliveryOption.DateTimePeriod>>(emptyMap())

    private val deliveryOptionsStateBuilder = DeliveryOptionsState.Builder()
    private val deliveryOptionsState = combine(
        deliveryOptionsResultFlow,
        deliveryOptionsRequester.loadingState,
        selectedDeliveryOptionId,
        deliveryOptionToSelectedDateTimePeriod,
    ) { result, loadingState, selectedDeliveryOptionId, deliveryOptionToSelectedDateTimePeriod ->
        deliveryOptionsStateBuilder.build(
            deliveryOptionsResult = result,
            isLoading = loadingState.isLoading(),
            selectedDeliveryOptionId = selectedDeliveryOptionId,
            deliveryOptionSelectedDateTimePeriodProvider = { deliveryOption ->
                deliveryOptionToSelectedDateTimePeriod[deliveryOption.id]
                    ?: deliveryOption.dateTimePeriods.firstOrNull()
            },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DeliveryOptionsState.None,
    )

    private val isContinueButtonVisible = deliveryOptionsState.map {
        it.findSelectedDeliveryOption() != null
    }

    private val initialDeliveryAddressSelectorState = DeliveryAddressSelectorState(
        deliveryType = deliveryType,
        city = null,
        streetSelectorTextFieldState = addressComponent.streetSelectorTextFieldState,
        buildingSelectorTextFieldState = addressComponent.buildingSelectorTextFieldState,
        apartmentSelectorTextFieldState = addressComponent.apartmentSelectorTextFieldState,
        isBuildingSelectionEnabled = false,
        deliveryOptionsState = deliveryOptionsState.value,
        isContinueButtonVisible = false,
    )

    init {
        handleDeliveryOptionDateTimeSelectorResult(deliveryOptionDateTimeSelectorResultFlow)
    }

    val deliveryAddressSelectorState: StateFlow<DeliveryAddressSelectorState> = combine(
        addressComponent.cityFlow,
        addressComponent.isBuildingSelectionEnabled,
        deliveryOptionsState,
        isContinueButtonVisible,
    ) { city, isBuildingSelectionEnabled, deliveryOptionsState, isContinueButtonVisible ->
        DeliveryAddressSelectorState(
            deliveryType = deliveryType,
            city = city,
            streetSelectorTextFieldState = addressComponent.streetSelectorTextFieldState,
            buildingSelectorTextFieldState = addressComponent.buildingSelectorTextFieldState,
            apartmentSelectorTextFieldState = addressComponent.apartmentSelectorTextFieldState,
            isBuildingSelectionEnabled = isBuildingSelectionEnabled,
            deliveryOptionsState = deliveryOptionsState,
            isContinueButtonVisible = isContinueButtonVisible,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = initialDeliveryAddressSelectorState,
    )

    private val visibleAddressSearchBottomSheetType = MutableStateFlow<AddressSearchType?>(null)

    val addressSearchBottomSheetState: StateFlow<AddressSearchBottomSheetState> =
        combine(
            visibleAddressSearchBottomSheetType,
            addressComponent.streetSearchState,
            addressComponent.buildingSearchState,
        ) { type, streetSearchState, buildingSearchState ->
            if (type != null) {
                val searchTextFieldState = when (type) {
                    AddressSearchType.Street -> addressComponent.streetSearchTextFieldState
                    AddressSearchType.Building -> addressComponent.buildingSearchTextFieldState
                }
                val searchState = when (type) {
                    AddressSearchType.Street -> streetSearchState
                    AddressSearchType.Building -> buildingSearchState
                }
                AddressSearchBottomSheetState.Visible(
                    type = type,
                    searchTextFieldState = searchTextFieldState,
                    searchState = searchState,
                )
            } else {
                AddressSearchBottomSheetState.Hidden
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = AddressSearchBottomSheetState.Hidden,
        )

    private val _genericBottomSheetState =
        MutableStateFlow<GenericBottomSheetState>(GenericBottomSheetState.Hidden)
    val genericBottomSheetState: StateFlow<GenericBottomSheetState> =
        _genericBottomSheetState.asStateFlow()

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
        }
    }

    fun onDeliveryAddressSelectorEvent(event: DeliveryAddressSelectorEvent) {
        when (event) {
            DeliveryAddressSelectorEvent.StreetSelectorClicked -> {
                visibleAddressSearchBottomSheetType.value = AddressSearchType.Street
            }

            DeliveryAddressSelectorEvent.BuildingSelectorClicked -> {
                visibleAddressSearchBottomSheetType.value = AddressSearchType.Building
            }

            is DeliveryAddressSelectorEvent.DeliveryOptionClicked -> {
                selectedDeliveryOptionId.value = event.deliveryOption.id
            }

            is DeliveryAddressSelectorEvent.DeliveryOptionDateClicked -> {
                onDeliveryOptionDateClicked(event)
            }

            is DeliveryAddressSelectorEvent.DeliveryOptionTimeClicked -> {
                onDeliveryOptionTimeClicked(event)
            }

            is DeliveryAddressSelectorEvent.ShowDeliveryOptionDetails -> {
                onShowDeliveryOptionDetails(event)
            }

            DeliveryAddressSelectorEvent.ContinueClicked -> onContinueClicked()

            DeliveryAddressSelectorEvent.ErrorRefreshClicked -> {
                deliveryOptionsRequester.request(DeliveryOptionsRequest)
            }

            DeliveryAddressSelectorEvent.GenericBottomSheetClosed -> {
                _genericBottomSheetState.value = GenericBottomSheetState.Hidden
            }
        }
    }

    fun onAddressSearchEvent(event: AddressSearchEvent) {
        when (event) {
            AddressSearchEvent.CloseClicked -> {
                visibleAddressSearchBottomSheetType.value = null
            }

            is AddressSearchEvent.AddressItemClicked -> {
                when (event.type) {
                    AddressSearchType.Street -> addressComponent.onStreetSelected(event.item)
                    AddressSearchType.Building -> addressComponent.onBuildingSelected(event.item)
                }
            }

            is AddressSearchEvent.ErrorRefreshClicked -> {
                when (event.type) {
                    AddressSearchType.Street -> addressComponent.onStreetSearchErrorRefreshClicked()
                    AddressSearchType.Building -> addressComponent.onBuildingSearchErrorRefreshClicked()
                }
            }
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

    private fun onDeliveryOptionDateClicked(event: DeliveryAddressSelectorEvent.DeliveryOptionDateClicked) {
        navigationThrottler.throttle {
            val datePeriods = event.deliveryOption.dateTimePeriods.distinctBy { it.date }
            val action = DeliveryAddressSelectorScreenAction.SelectDeliveryOptionDateTimeClicked(
                type = DeliveryOptionDateTimeType.DATE,
                deliveryOption = event.deliveryOption,
                dateTimePeriods = datePeriods,
            )
            emitSideEffect(DeliveryAddressSelectorSideEffect.Navigate(action))
        }
    }

    private fun onDeliveryOptionTimeClicked(event: DeliveryAddressSelectorEvent.DeliveryOptionTimeClicked) {
        navigationThrottler.throttle {
            val option = event.deliveryOption
            val selectedDateTimePeriod =
                deliveryOptionToSelectedDateTimePeriod.value[option.id]
                    ?: option.dateTimePeriods.firstOrNull()
            val timePeriods = option.dateTimePeriods.filter {
                it.date == selectedDateTimePeriod?.date
            }
            val action = DeliveryAddressSelectorScreenAction.SelectDeliveryOptionDateTimeClicked(
                type = DeliveryOptionDateTimeType.TIME,
                deliveryOption = event.deliveryOption,
                dateTimePeriods = timePeriods,
            )
            emitSideEffect(DeliveryAddressSelectorSideEffect.Navigate(action))
        }
    }

    private fun onShowDeliveryOptionDetails(event: DeliveryAddressSelectorEvent.ShowDeliveryOptionDetails) {
        val bottomSheetState = GenericBottomSheetState.Visible(
            body = Text.String(event.deliveryOption.description),
            buttonState = GenericBottomSheetState.ButtonState(
                text = Text.Resource(RCommon.string.res_understood),
            ),
        )
        _genericBottomSheetState.value = bottomSheetState
    }

    private fun onContinueClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val address = addressComponent.currentAddress.firstOrNull()
                val selectedDeliveryOption = deliveryOptionsState.value.findSelectedDeliveryOption()

                if (address != null && selectedDeliveryOption != null) {
                    val cartType = navEntry.cartType.toCartType()
                    val deliveryMethod = navEntry.deliveryMethod.toDeliveryMethod()
                    val recipient = navEntry.recipient.toRecipient()
                    val checkoutParams = when (deliveryType) {
                        DeliveryType.COURIER -> {
                            CourierDeliveryCheckoutParams(
                                cartType = cartType,
                                deliveryMethod = deliveryMethod,
                                recipient = recipient,
                                address = address,
                                deliveryOption = selectedDeliveryOption.deliveryOption,
                                dateTimePeriod = selectedDeliveryOption.selectedDateTimePeriod,
                            )
                        }

                        DeliveryType.POST -> {
                            PostDeliveryCheckoutParams(
                                cartType = cartType,
                                deliveryMethod = deliveryMethod,
                                recipient = recipient,
                                address = address,
                                deliveryOption = selectedDeliveryOption.deliveryOption,
                                dateTimePeriod = selectedDeliveryOption.selectedDateTimePeriod,
                            )
                        }
                    }
                    val action = DeliveryAddressSelectorScreenAction.ContinueClicked(
                        currentCheckoutStep = navEntry.checkoutStep,
                        checkoutParams = checkoutParams,
                    )
                    emitSideEffect(DeliveryAddressSelectorSideEffect.Navigate(action))
                }
            }
        }
    }

    private fun getDeliveryType(deliveryMethod: DeliveryMethod): DeliveryType {
        return when (deliveryMethod.type) {
            DeliveryMethodType.COURIER_EXPRESS -> DeliveryType.COURIER
            DeliveryMethodType.POST -> DeliveryType.POST
            else -> error("Delivery method $deliveryMethod is not supported")
        }
    }

    private fun handleDeliveryOptionDateTimeSelectorResult(
        resultFlow: Flow<DeliveryOptionDateTimeSelectorScreenResult?>,
    ) {
        viewModelScope.launch {
            screenResultHandler.handle(
                resultFlow = resultFlow,
                key = Keys.DELIVERY_OPTION_DATE_TIME_SELECTOR_RESULT.key,
            ) { result ->
                val deliveryOptionId = result.getDeliveryOptionId()
                deliveryOptionToSelectedDateTimePeriod.update {
                    it + (deliveryOptionId to result.dateTimePeriod.toDateTimePeriod())
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            deliveryOptionDateTimeSelectorResultFlow: Flow<DeliveryOptionDateTimeSelectorScreenResult?>,
        ): DeliveryAddressSelectorViewModel
    }

    private data object DeliveryOptionsRequest : FlowRequest

    private enum class Keys {
        DELIVERY_OPTION_DATE_TIME_SELECTOR_RESULT;

        val key: String get() = name
    }

    private companion object {
        private const val TAG = "DeliveryAddressSelectorViewModel"
    }
}
