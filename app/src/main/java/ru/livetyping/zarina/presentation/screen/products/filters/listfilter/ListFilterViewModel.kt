package ru.livetyping.zarina.presentation.screen.products.filters.listfilter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.ListFilterItem
import ru.livetyping.zarina.domain.filter.copy
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.filter.ListFilterParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ListFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    interactor: ListFilterInteractor,
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

    val isApplyButtonVisible: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_IS_APPLY_BUTTON_VISIBLE,
        initialValue = false,
    )

    // TODO: [Medium] Get city from ListFilterItem?
    val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

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

            savedStateHandle[KEY_IS_APPLY_BUTTON_VISIBLE] = true
        }
    }

    fun onItemClicked(item: ListFilterItem) {
        val currentFilter = filter.value
        val newItems = currentFilter.items.map {
            when {
                it.id == item.id -> {
                    val isSelected =
                        if (filter.value.type != Filter.Type.SORTING) !item.isSelected else true
                    it.copy(isSelected = isSelected)
                }
                currentFilter.isSingleSelection -> it.copy(isSelected = false)
                else -> it
            }
        }
        val newFilter = currentFilter.copy(items = newItems)
        savedStateHandle[KEY_FILTER] = ListFilterParcelable.from(newFilter)

        if (filter.value.type != Filter.Type.SORTING) {
            savedStateHandle[KEY_IS_APPLY_BUTTON_VISIBLE] = true
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
        private const val KEY_IS_APPLY_BUTTON_VISIBLE = "is_apply_button_visible"
    }
}
