package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableEvent
import javax.inject.Inject

@HiltViewModel
internal class SizeTableViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SizeTableSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<SizeTableNavEntry>(
        typeMap = SizeTableNavEntry.typeMap(),
    )
    private val productMeasurements = navEntry.getProductMeasurements()
    private val sizeGuide = navEntry.getSizeGuide()

    fun onSizeTableEvent(event: SizeTableEvent) {
        when (event) {
            SizeTableEvent.CloseClicked -> onCloseClicked()
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = SizeTableScreenAction.CloseClicked
            emitSideEffect(SizeTableSideEffect.Navigate(action))
        }
    }
}
