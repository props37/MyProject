package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.domain.rework.filter.Filter
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem
import ru.zarina.zarina.domain.rework.filter.copy
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ListFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val initialFilter: StateFlow<ListFilter<ListFilterItem>> = savedStateHandle
        .getStateFlow<ListFilterParcelable?>(
            key = UnscopedDestinations.ListFilter.ARG_KEY_FILTER,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "Filter is null" }
            parcelable.toListFilter()
        }

    val filter: StateFlow<ListFilter<ListFilterItem>> = savedStateHandle
        .getStateFlow<ListFilterParcelable?>(
            key = KEY_FILTER,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            parcelable?.toListFilter() ?: initialFilter.value
        }

    val isResetButtonVisible: StateFlow<Boolean> = filter.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { filter ->
        filter.type != Filter.Type.SORTING && filter.selectedItems.isNotEmpty()
    }

    private val _isApplyButtonVisible = MutableStateFlow(false)
    val isApplyButtonVisible: StateFlow<Boolean> = _isApplyButtonVisible.asStateFlow()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val result = ListFilterScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onResetClicked() {
        if (filter.value.type != Filter.Type.SORTING) {
            val currentFilter = filter.value
            val newItems = currentFilter.items.map {
                if (it.isSelected) it.copy(isSelected = false) else it
            }
            val newFilter = currentFilter.copy(items = newItems)
            savedStateHandle[KEY_FILTER] = ListFilterParcelable.from(newFilter)

            _isApplyButtonVisible.value = true
        }
    }

    fun onItemClicked(item: ListFilterItem) {
        val currentFilter = filter.value
        val newItems = currentFilter.items.map {
            when {
                it.id == item.id -> it.copy(isSelected = !item.isSelected)
                currentFilter.isSingleSelection -> it.copy(isSelected = false)
                else -> it
            }
        }
        val newFilter = currentFilter.copy(items = newItems)
        savedStateHandle[KEY_FILTER] = ListFilterParcelable.from(newFilter)

        if (filter.value.type != Filter.Type.SORTING) {
            _isApplyButtonVisible.value = true
        } else {
            val result = ListFilterScreenResult.FilterChanged(filter.value)
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onApplyClicked() {
        navigationThrottler.throttle {
            val result = ListFilterScreenResult.FilterChanged(filter.value)
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: ListFilterScreenResult) : SideEffect
    }

    companion object {
        private const val KEY_FILTER = "filter"
    }
}
