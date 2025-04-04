package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.component.AddressComponent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchType
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

    private val initialDeliveryAddressSelectorState = DeliveryAddressSelectorState(
        deliveryType = deliveryType,
        city = null,
        streetSelectorTextFieldState = addressComponent.streetSelectorTextFieldState,
        buildingSelectorTextFieldState = addressComponent.buildingSelectorTextFieldState,
        apartmentSelectorTextFieldState = addressComponent.apartmentSelectorTextFieldState,
        isBuildingSelectionEnabled = false,
    )

    val deliveryAddressSelectorState: StateFlow<DeliveryAddressSelectorState> = combine(
        addressComponent.cityFlow,
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

    private val visibleAddressSearchBottomSheetType = MutableStateFlow<AddressSearchType?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val addressSearchBottomSheetState: StateFlow<AddressSearchBottomSheetState> =
        visibleAddressSearchBottomSheetType.flatMapLatest { type ->
            if (type != null) {
                val searchTextFieldState = when (type) {
                    AddressSearchType.Street -> addressComponent.streetSearchTextFieldState
                    AddressSearchType.Building -> addressComponent.buildingSearchTextFieldState
                }
                val searchStateFlow = when (type) {
                    AddressSearchType.Street -> addressComponent.streetSearchState
                    AddressSearchType.Building -> addressComponent.buildingSearchState
                }
                searchStateFlow.map { searchState ->
                    AddressSearchBottomSheetState.Visible(
                        type = type,
                        searchTextFieldState = searchTextFieldState,
                        searchState = searchState,
                    )
                }
            } else {
                flowOf(AddressSearchBottomSheetState.Hidden)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = AddressSearchBottomSheetState.Hidden,
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
            DeliveryAddressSelectorEvent.StreetSelectorClicked -> {
                visibleAddressSearchBottomSheetType.value = AddressSearchType.Street
            }

            DeliveryAddressSelectorEvent.BuildingSelectorClicked -> {
                visibleAddressSearchBottomSheetType.value = AddressSearchType.Building
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
