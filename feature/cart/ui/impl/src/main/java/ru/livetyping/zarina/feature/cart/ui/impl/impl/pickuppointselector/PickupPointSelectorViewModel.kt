package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import android.Manifest
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.location.CurrentLocationComponent
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.Filter
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointListState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointListStateBuilder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ToggleableFilter
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ViewMode
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class PickupPointSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: PickupPointSelectorDependencies,
) : ViewModel(), SideEffectSource<PickupPointSelectorSideEffect> by SideEffectSourceImpl() {

    private val currentLocationComponent = CurrentLocationComponent(
        coroutineScope = viewModelScope,
        getCurrentLocationFlowUseCase = deps.getCurrentLocationFlow,
    )

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<PickupPointSelectorNavEntry>(
        typeMap = PickupPointSelectorNavEntry.typeMap(),
    )
    private val cartType = navEntry.cartType.toCartType()

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        value = CheckoutTopBarState(
            checkoutStep = navEntry.checkoutStep,
            checkoutStepCount = cartType.checkoutStepCount,
            isBackButtonVisible = true,
        )
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val filterTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val availableFilters = Filter.entries.toList()
    private val appliedFilters = MutableStateFlow(setOf<Filter>())
    private val filters = appliedFilters.map { appliedFilters ->
        availableFilters
            .map { filter ->
                ToggleableFilter(
                    filter = filter,
                    isApplied = filter in appliedFilters,
                )
            }
            .toImmutableList()
    }

    private val viewModes = persistentListOf(ViewMode.MAP, ViewMode.LIST)
    private val currentViewMode = MutableStateFlow(ViewMode.MAP)
    private val viewModeSelectorState = currentViewMode.map { currentViewMode ->
        TabRowState(viewModes, currentViewMode)
    }

    private val initialPickupPointSelectorState = PickupPointSelectorState(
        filterTextFieldState = filterTextFieldState,
        filters = persistentListOf(),
        viewModeSelectorState = TabRowState(viewModes, currentViewMode.value),
        pickupPointListState = PickupPointListState.Loading,
    )

    private val pickupPointRequester = FlowRequester(PickupPointRequest) {
        deps.getPickupPointsFlow()
    }

    private val pickupPointListStateBuilder = PickupPointListStateBuilder()
    private val pickupPointListState = combine(
        pickupPointRequester.flow,
        pickupPointRequester.loadingState,
        filterTextFieldState.textAsFlow(),
        appliedFilters,
    ) { pickupPointResult, pickupPointLoadingState, filterQuery, appliedFilters ->
        pickupPointListStateBuilder.build(
            pickupPointResult = pickupPointResult,
            pickupPointLoadingState = pickupPointLoadingState,
            filterQuery = filterQuery.toString(),
            appliedFilters = appliedFilters.toList(),
        )
    }

    val pickupPointSelectorState: StateFlow<PickupPointSelectorState> = combine(
        filters,
        viewModeSelectorState,
        pickupPointListState,
    ) { filters, viewModeSelectorState, pickupPointListState ->
        PickupPointSelectorState(
            filterTextFieldState = filterTextFieldState,
            filters = filters,
            viewModeSelectorState = viewModeSelectorState,
            pickupPointListState = pickupPointListState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = initialPickupPointSelectorState,
    )

    val currentLocation: StateFlow<Location?> = currentLocationComponent.currentLocation

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
        }
    }

    fun onPickupPointSelectorEvent(event: PickupPointSelectorEvent) {
        when (event) {
            is PickupPointSelectorEvent.FilterClicked -> onFilterClicked(event)
            is PickupPointSelectorEvent.ViewModeSelectorEvent -> onViewModeSelectorEvent(event)
            is PickupPointSelectorEvent.PickupPointClicked -> TODO() // TODO: [Top] Implement
            PickupPointSelectorEvent.ErrorRefreshClicked -> onErrorRefreshClicked()
            PickupPointSelectorEvent.MyLocationClicked -> onMyLocationClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PickupPointSelectorScreenAction.BackClicked
            emitSideEffect(PickupPointSelectorSideEffect.Navigate(action))
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = PickupPointSelectorScreenAction.CloseClicked
            emitSideEffect(PickupPointSelectorSideEffect.Navigate(action))
        }
    }

    private fun onFilterClicked(event: PickupPointSelectorEvent.FilterClicked) {
        appliedFilters.update { appliedFilters ->
            val filter = event.filter.filter
            if (filter in appliedFilters) appliedFilters - filter else appliedFilters + filter
        }
    }

    private fun onViewModeSelectorEvent(event: PickupPointSelectorEvent.ViewModeSelectorEvent) {
        when (event.event) {
            is TabRowEvent.TabChanged<ViewMode> -> currentViewMode.value = event.event.tab
            is TabRowEvent.TabReselected<ViewMode> -> Unit
        }
    }

    private fun onErrorRefreshClicked() {
        pickupPointRequester.request(PickupPointRequest)
    }

    private fun onMyLocationClicked() {
        val permissionManager = deps.permissionManager
        viewModelScope.launch {
            val fineLocationPermissionState =
                permissionManager.getPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            if (fineLocationPermissionState.isGranted) {
                currentLocationComponent.refreshCurrentLocation()
            } else {
                val newPermissionState =
                    permissionManager.requestMultiplePermissions(LOCATION_PERMISSIONS)
                if (newPermissionState.any { it.value.isGranted }) {
                    currentLocationComponent.refreshCurrentLocation()
                } else {
                    // TODO: [Top] Show PermissionRequired dialog
                }
            }
        }
    }

    private data object PickupPointRequest : FlowRequest

    private companion object {
        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
