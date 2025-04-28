package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.GenderPickerComponent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogState
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(
    private val deps: CatalogDependencies,
) : ViewModel(), SideEffectSource<CatalogSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val genderPickerComponent = GenderPickerComponent(viewModelScope)

    private val catalogInitialState = CatalogState(
        genderPickerState = genderPickerComponent.genderPickerState.value,
    )

    val catalogState: StateFlow<CatalogState> = combine(
        flowOf(Unit),
        genderPickerComponent.genderPickerState,
    ) { _, genderPickerState ->
        CatalogState(genderPickerState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = catalogInitialState,
    )

    fun onCatalogEvent(event: CatalogEvent) {
        when (event) {
            CatalogEvent.BackClicked -> onBackClicked()
            is CatalogEvent.GenderSelected -> genderPickerComponent.onGenderSelected(event.tab)
            CatalogEvent.SearchClicked -> onSearchClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.BackClicked
            emitSideEffect(CatalogSideEffect.Navigate(action))
        }
    }

    private fun onSearchClicked() {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.SearchClicked
            emitSideEffect(CatalogSideEffect.Navigate(action))
        }
    }
}
