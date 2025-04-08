package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

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

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = DeliveryOptionDateTimeSelectorScreenAction.BackClicked
            emitSideEffect(DeliveryOptionDateTimeSelectorSideEffect.Navigate(action))
        }
    }
}
