package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.checkout.CourierDeliveryOptions
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.checkout.CourierDeliveryDateTimePeriodParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutCourierDeliveryDateTimeSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val selectorType: StateFlow<CourierDeliveryDateTimeSelectorType> = savedStateHandle
        .getStateFlow<CourierDeliveryDateTimeSelectorType?>(
            key = CheckoutGraph.CourierDeliveryDateTimeSelector.ARG_SELECTOR_TYPE,
            initialValue = null
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "selectorType is null" }
        }

    private val dateTimePeriods: StateFlow<List<CourierDeliveryOptions.Option.DateTimePeriod>> =
        savedStateHandle
            .getStateFlow<Array<CourierDeliveryDateTimePeriodParcelable>?>(
                key = CheckoutGraph.CourierDeliveryDateTimeSelector.ARG_DATE_TIME_PERIODS,
                initialValue = null,
            )
            .mapState(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
            ) { array ->
                checkNotNull(array) { "dateTimePeriods is null" }
                array.map { it.toDateTimePeriod() }
            }

    private val selectedDateTimePeriodId =
        MutableStateFlow<CourierDeliveryOptions.Option.DateTimePeriod.Id?>(null)

    val items: StateFlow<List<Item>> = combine(
        dateTimePeriods,
        selectedDateTimePeriodId,
        selectorType,
    ) { dateTimePeriods, selectedDateTimePeriodId, selectorType ->
        createItems(dateTimePeriods, selectedDateTimePeriodId, selectorType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = createItems(
            dateTimePeriods = dateTimePeriods.value,
            selectedDateTimePeriodId = selectedDateTimePeriodId.value,
            selectorType = selectorType.value,
        ),
    )

    val isContinueButtonVisible: StateFlow<Boolean> = selectedDateTimePeriodId.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) {
        it != null
    }
    
    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutCourierDeliveryDateTimeSelectorScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onItemClicked(item: Item) {
        selectedDateTimePeriodId.value = item.dateTimePeriodId
    }

    fun onContinueClicked() {
        // TODO: [High] Implement
    }

    private fun createItems(
        dateTimePeriods: List<CourierDeliveryOptions.Option.DateTimePeriod>,
        selectedDateTimePeriodId: CourierDeliveryOptions.Option.DateTimePeriod.Id?,
        selectorType: CourierDeliveryDateTimeSelectorType,
    ): List<Item> {
        return dateTimePeriods.map { period ->
            val text = when (selectorType) {
                CourierDeliveryDateTimeSelectorType.DATE -> period.date
                CourierDeliveryDateTimeSelectorType.TIME -> period.time
            }
            Item(
                dateTimePeriodId = period.id,
                text = text,
                isSelected = period.id == selectedDateTimePeriodId,
            )
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutCourierDeliveryDateTimeSelectorScreenAction) : SideEffect
    }

    @Immutable
    data class Item(
        val dateTimePeriodId: CourierDeliveryOptions.Option.DateTimePeriod.Id,
        val text: String,
        val isSelected: Boolean,
    )
}
