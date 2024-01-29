package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ListFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val initialFilter: StateFlow<ListFilter<*>> = savedStateHandle
        .getStateFlow<ListFilterParcelable?>(
            key = UnscopedDestinations.ListFilter.ARG_KEY_FILTER,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "Filter is null" }
            it.toListFilter()
        }

    private val _filter = MutableStateFlow(initialFilter.value)
    val filter = _filter.asStateFlow()

    fun onBackClicked() {
        // TODO: [High] Implement
    }

    fun onResetClicked() {
        // TODO: [High] Implement
    }

    sealed interface SideEffect : SideEffectSource.SideEffect
}
