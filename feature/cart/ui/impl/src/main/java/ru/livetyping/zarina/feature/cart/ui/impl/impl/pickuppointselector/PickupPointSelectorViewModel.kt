package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

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
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.Filter
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ToggleableFilter
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ViewMode
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject

@HiltViewModel
internal class PickupPointSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<PickupPointSelectorSideEffect> by SideEffectSourceImpl() {

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
    )

    val pickupPointSelectorState: StateFlow<PickupPointSelectorState> = combine(
        filters,
        viewModeSelectorState,
    ) { filters, viewModeSelectorState ->
        PickupPointSelectorState(
            filterTextFieldState = filterTextFieldState,
            filters = filters,
            viewModeSelectorState = viewModeSelectorState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = initialPickupPointSelectorState,
    )

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
}
