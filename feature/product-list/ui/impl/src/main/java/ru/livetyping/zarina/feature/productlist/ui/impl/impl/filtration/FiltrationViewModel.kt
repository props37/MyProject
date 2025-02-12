package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.filtration.FiltrationComponent
import javax.inject.Inject

@HiltViewModel
internal class FiltrationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<FiltrationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<FiltrationNavEntry>(
        typeMap = FiltrationNavEntry.typeMap(),
    )
    private val categoryId = navEntry.getCategoryId()
    private val initialFilters = navEntry.filters?.toFilters()

    private val filtrationComponent = FiltrationComponent(
        savedStateHandle = savedStateHandle,
        initialFilters = initialFilters,
        coroutineScope = viewModelScope,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = FiltrationScreenAction.BackClicked
            emitSideEffect(FiltrationSideEffect.Navigate(action))
        }
    }
}
