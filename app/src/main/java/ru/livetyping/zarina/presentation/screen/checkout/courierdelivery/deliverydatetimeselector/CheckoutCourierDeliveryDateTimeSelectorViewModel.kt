package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutCourierDeliveryDateTimeSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val params = savedStateHandle.toRoute<CheckoutGraph.CourierDeliveryDateTimeSelector>(
        typeMap = CheckoutGraph.CourierDeliveryDateTimeSelector.typeMap(),
    )

    val selectorType: StateFlow<CourierDeliveryDateTimeSelectorType> =
        ImmutableStateFlow(params.type)

    private val dateTimePeriods = params.dateTimePeriods.map { it.toDateTimePeriod() }

    private val selectedDateTimePeriodId =
        MutableStateFlow<DeliveryOption.DateTimePeriod.Id?>(null)

    val items: StateFlow<List<Item>> = combine(
        selectedDateTimePeriodId,
        selectorType,
    ) { selectedDateTimePeriodId, selectorType ->
        createItems(selectedDateTimePeriodId, selectorType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = createItems(
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
        val dateTimePeriod = dateTimePeriods.find {
            it.id == selectedDateTimePeriodId.value
        }

        if (dateTimePeriod != null) {
            navigationThrottler.throttle {
                val action = CheckoutCourierDeliveryDateTimeSelectorScreenAction.DateTimePeriodSelected(
                    deliveryOptionId = DeliveryOption.Id(params.deliveryOptionId),
                    selectorType = selectorType.value,
                    dateTimePeriod = dateTimePeriod,
                )
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            val messageText = Text.Resource(R.string.something_went_wrong)
            val message = ZarinaToastMessage.error(messageText)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
        }
    }

    private fun createItems(
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

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Immutable
    data class Item(
        val dateTimePeriodId: DeliveryOption.DateTimePeriod.Id,
        val text: String,
        val isSelected: Boolean,
    )
}
