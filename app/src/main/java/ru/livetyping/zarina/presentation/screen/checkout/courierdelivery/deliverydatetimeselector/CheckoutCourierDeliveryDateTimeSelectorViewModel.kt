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
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.checkout.DeliveryDateTimePeriodParcelable
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

    private val deliveryOptionId: StateFlow<DeliveryOption.Id> =
        savedStateHandle
            .getStateFlow<String?>(
                key = CheckoutGraph.CourierDeliveryDateTimeSelector.ARG_DELIVERY_OPTION_ID,
                initialValue = null,
            )
            .mapState(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
            ) {
                checkNotNull(it) { "deliveryOptionId is null" }
                DeliveryOption.Id(it)
            }

    private val dateTimePeriods: StateFlow<List<DeliveryOption.DateTimePeriod>> =
        savedStateHandle
            .getStateFlow<Array<DeliveryDateTimePeriodParcelable>?>(
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
        MutableStateFlow<DeliveryOption.DateTimePeriod.Id?>(null)

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
        navigationThrottler.throttle {
            val dateTimePeriod = dateTimePeriods.value.find {
                it.id == selectedDateTimePeriodId.value
            } ?: return@throttle
            val action = CheckoutCourierDeliveryDateTimeSelectorScreenAction.DateTimePeriodSelected(
                deliveryOptionId = deliveryOptionId.value,
                selectorType = selectorType.value,
                dateTimePeriod = dateTimePeriod,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun createItems(
        dateTimePeriods: List<DeliveryOption.DateTimePeriod>,
        selectedDateTimePeriodId: DeliveryOption.DateTimePeriod.Id?,
        selectorType: CourierDeliveryDateTimeSelectorType,
    ): List<Item> {
        return dateTimePeriods.map { period ->
            val text = when (selectorType) {
                CourierDeliveryDateTimeSelectorType.DATE -> period.date
                CourierDeliveryDateTimeSelectorType.TIME -> period.time
            }
            Item(
                dateTimePeriodId = period.id,
                text = text.orEmpty(),
                isSelected = period.id == selectedDateTimePeriodId,
            )
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutCourierDeliveryDateTimeSelectorScreenAction) : SideEffect
    }

    @Immutable
    data class Item(
        val dateTimePeriodId: DeliveryOption.DateTimePeriod.Id,
        val text: String,
        val isSelected: Boolean,
    )
}
