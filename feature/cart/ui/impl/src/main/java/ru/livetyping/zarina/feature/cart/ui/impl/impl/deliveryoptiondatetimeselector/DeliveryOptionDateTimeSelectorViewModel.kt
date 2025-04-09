package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model.DateTimeItem
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model.DateTimeSelectorState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class DeliveryOptionDateTimeSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    SideEffectSource<DeliveryOptionDateTimeSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<DeliveryOptionDateTimeSelectorNavEntry>(
        typeMap = DeliveryOptionDateTimeSelectorNavEntry.typeMap(),
    )
    private val selectorType = navEntry.type
    private val deliveryOptionId = navEntry.getDeliveryOptionId()
    private val dateTimePeriods = navEntry.dateTimePeriods.map { it.toDateTimePeriod() }

    private val selectedDateTimePeriodId = MutableStateFlow<DeliveryOption.DateTimePeriod.Id?>(null)

    private val dateTimeItems = selectedDateTimePeriodId.map { selectedPeriodId ->
        getDateTimeItems(dateTimePeriods, selectedPeriodId).toImmutableList()
    }

    private val initialDateTimeItems =
        getDateTimeItems(dateTimePeriods, selectedDateTimePeriodId.value)

    val dateTimeSelectorState = combine(
        dateTimeItems,
        selectedDateTimePeriodId,
    ) { dateTimeItems, selectedDateTimePeriodId ->
        DateTimeSelectorState(
            selectorType = selectorType,
            dateTimeItems = dateTimeItems,
            isContinueButtonVisible = selectedDateTimePeriodId != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = DateTimeSelectorState(
            selectorType = selectorType,
            dateTimeItems = initialDateTimeItems.toImmutableList(),
            isContinueButtonVisible = false,
        ),
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = DeliveryOptionDateTimeSelectorScreenAction.BackClicked
            emitSideEffect(DeliveryOptionDateTimeSelectorSideEffect.Navigate(action))
        }
    }

    fun onDateTimeItemClicked(item: DateTimeItem) {
        selectedDateTimePeriodId.value = item.dateTimePeriod.id
    }

    fun onContinueClicked() {
        val dateTimePeriod = dateTimePeriods.find {
            it.id == selectedDateTimePeriodId.value
        }

        if (dateTimePeriod != null) {
            navigationThrottler.throttle {
                val action = DeliveryOptionDateTimeSelectorScreenAction.DateTimePeriodSelected(
                    selectorType = selectorType,
                    deliveryOptionId = deliveryOptionId,
                    dateTimePeriod = dateTimePeriod,
                )
                emitSideEffect(DeliveryOptionDateTimeSelectorSideEffect.Navigate(action))
            }
        } else {
            val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
            val message = ZarinaToastMessage.error(messageText)
            emitSideEffect(DeliveryOptionDateTimeSelectorSideEffect.ShowZarinaToast(message))
        }
    }

    private fun getDateTimeItems(
        dateTimePeriods: List<DeliveryOption.DateTimePeriod>,
        selectedDateTimePeriodId: DeliveryOption.DateTimePeriod.Id?,
    ): List<DateTimeItem> {
        return dateTimePeriods
            .map { period ->
                val text = when (selectorType) {
                    DeliveryOptionDateTimeSelectorType.DATE -> period.date
                    DeliveryOptionDateTimeSelectorType.TIME -> period.time.orEmpty()
                }
                DateTimeItem(
                    dateTimePeriod = period,
                    text = text,
                    isSelected = period.id == selectedDateTimePeriodId,
                )
            }
    }
}
