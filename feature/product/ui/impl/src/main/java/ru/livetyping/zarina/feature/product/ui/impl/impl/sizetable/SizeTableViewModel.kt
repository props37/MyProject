package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ViewMode
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
    private val sizeGuide = navEntry.getSizeGuide().let {
        it.copy(entries = it.entries.sortedBy { it.sizeRu.size })
    }
    private val gender = navEntry.getGender()

    private val viewModes = ViewMode.entries.toImmutableList()
    private val currentViewMode = MutableStateFlow(ViewMode.PRODUCT_MEASUREMENTS)
    private val viewModeSelectorState = currentViewMode.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        transform = { TabRowState(viewModes, it) },
    )

    private val initialSizeTableState = SizeTableState(
        viewModeSelectorState = viewModeSelectorState.value,
        sizeGuide = sizeGuide,
        gender = gender,
    )

    val sizeTableState: StateFlow<SizeTableState> = combine(
        viewModeSelectorState,
        flowOf(Unit),
    ) { viewModeSelectorState, _ ->
        SizeTableState(
            viewModeSelectorState = viewModeSelectorState,
            sizeGuide = sizeGuide,
            gender = gender,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = initialSizeTableState,
    )

    fun onSizeTableEvent(event: SizeTableEvent) {
        when (event) {
            SizeTableEvent.CloseClicked -> onCloseClicked()
            is SizeTableEvent.ViewModeSelected -> onViewModeSelected(event)
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = SizeTableScreenAction.CloseClicked
            emitSideEffect(SizeTableSideEffect.Navigate(action))
        }
    }

    private fun onViewModeSelected(event: SizeTableEvent.ViewModeSelected) {
        currentViewMode.value = event.mode
    }
}
