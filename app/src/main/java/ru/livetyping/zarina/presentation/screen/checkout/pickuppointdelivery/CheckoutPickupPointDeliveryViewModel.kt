package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import android.Manifest
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.permissionmanager.isGranted
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetPickupPointsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutPickupPointDeliveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutPickupPointDeliveryInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val permissionManager = interactor.permissionManager

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

    @OptIn(SavedStateHandleSaveableApi::class)
    val nameOrAddressFilterTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val appliedFilters = MutableStateFlow(setOf<Filter>())

    val filters: StateFlow<List<ToggleableFilter>> = appliedFilters
        .map { applied ->
            Filter.entries.map { filter ->
                ToggleableFilter(filter = filter, isApplied = filter in applied)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = Filter.entries.map { ToggleableFilter(filter = it, isApplied = false) },
        )

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
        nameOrAddressFilterTextFieldState.textAsFlow(),
        appliedFilters,
    ) { result, loadingState, nameOrAddress, filters ->
        createPickupPointsState(result, loadingState, nameOrAddress, filters.toList())
    }.stateIn(
        scope = viewModelScope + Dispatchers.Default,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = PickupPointsState.Loading,
    )

    private val currentLocationRequester = FlowRequester(LocationRequest) {
        interactor.getCurrentLocationFlow()
    }

    val currentLocation: StateFlow<Location?> = currentLocationRequester.flow
        .map { result ->
            result.getOrNull()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
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

    fun onFilterClicked(filter: ToggleableFilter) {
        appliedFilters.update { applied ->
            if (filter.filter in applied) applied - filter.filter else applied + filter.filter
        }
    }

    fun onPickupPointsErrorRefreshClicked() {
        pickupPointsRequester.request(PickupPointsRequest)
    }

    fun onPickupPointClicked(pickupPoint: PickupPoint) {
        navigationThrottler.throttle {
            val action = CheckoutPickupPointDeliveryScreenAction.PickupPointSelected(
                pickupPoint = pickupPoint,
                cartType = cartType.value,
                step = step.value,
                deliveryMethodType = deliveryMethodType.value,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onMyLocationClicked() {
        viewModelScope.launch {
            val fineLocationPermissionState =
                permissionManager.getPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            if (fineLocationPermissionState.isGranted) {
                currentLocationRequester.request(LocationRequest)
            } else {
                val newPermissionsState =
                    permissionManager.requestMultiplePermissions(LOCATION_PERMISSIONS)
                if (newPermissionsState.any { it.value.isGranted }) {
                    currentLocationRequester.request(LocationRequest)
                } else {
                    val action = CheckoutPickupPointDeliveryScreenAction.LocationPermissionRequired
                    emitSideEffect(SideEffect.Navigate(action))
                }
            }
        }
    }

    private fun createPickupPointsState(
        pickupPointsResult: Result<List<PickupPoint>>?,
        loadingState: FlowRequester.LoadingState,
        nameOrAddress: CharSequence,
        filters: List<Filter>,
    ): PickupPointsState {
        return if (pickupPointsResult == null || loadingState.isLoading()) {
            PickupPointsState.Loading
        } else {
            pickupPointsResult.fold(
                onSuccess = { pickupPoints ->
                    val filteredPickupPoints = filterPickupPoints(
                        pickupPoints = pickupPoints,
                        nameOrAddress = nameOrAddress,
                        filters = filters,
                    )
                    PickupPointsState.Success(filteredPickupPoints)
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    PickupPointsState.Error(errorState)
                },
            )
        }
    }

    private fun filterPickupPoints(
        pickupPoints: List<PickupPoint>,
        nameOrAddress: CharSequence,
        filters: List<Filter>,
    ): List<PickupPoint> {
        return pickupPoints.filter { pickupPoint ->
            var matchFilters = true
            filters.forEach { filter ->
                when (filter) {
                    Filter.PAYMENT_BY_CARD -> {
                        if (!pickupPoint.isPaymentByCardAvailable) {
                            matchFilters = false
                            return@forEach
                        }
                    }

                    Filter.FITTING -> {
                        if (!pickupPoint.isFittingAvailable) {
                            matchFilters = false
                            return@forEach
                        }
                    }
                }
            }
            if (!matchFilters) return@filter false

            pickupPoint.title.contains(nameOrAddress, ignoreCase = true)
                    || pickupPoint.address.contains(nameOrAddress, ignoreCase = true)
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutPickupPointDeliveryScreenAction) : SideEffect
    }

    enum class Filter { PAYMENT_BY_CARD, FITTING }

    data class ToggleableFilter(
        val filter: Filter,
        val isApplied: Boolean,
    )

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

    private data object LocationRequest : FlowRequester.Request

    companion object {
        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
