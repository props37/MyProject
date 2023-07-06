package ru.zarina.zarina.ui.screens.catalog.filters.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.ListFilter
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.filters.FilterType
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersViewModel

@KoinViewModel
class ListFilterViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val parentSavedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<ListFilterViewModel.SideEffect> by SideEffectQueue() {

    private val newFiltration =
        parentSavedStateHandle.getStateFlow<Filtration?>(
            key = FiltersViewModel.KEY_NEW_FILTRATION,
            initialValue = null
        )

    val filterData = MutableStateFlow<ListFilter?>(null)

    init {
        newFiltration
            .onEach { filtration ->
                when (savedStateHandle.get<FilterType>(Catalog.Products.ARGUMENT_CATEGORY_ID)) {
                    FilterType.COLOR -> filterData.value = filtration?.colors
                    else -> Unit
                }
            }
    }

    fun onItemClick(item: ListFilter.Item) {
        filterData.update { filter ->
            filter?.copy(items = filter.items.map { if (it == item) item.copy(isSelected = !item.isSelected) else it })
        }
    }

    fun onClearClick() {
        filterData.update { filter ->
            filter?.copy(items = filter.items.map { it.copy(isSelected = false) })
        }
    }

    fun onApplyClick() {
        parentSavedStateHandle[FiltersViewModel.KEY_NEW_FILTRATION] =
            when (savedStateHandle.get<FilterType>(Catalog.Products.ARGUMENT_CATEGORY_ID)) {
                FilterType.COLOR -> newFiltration.value?.copy(colors = filterData.value)
                else -> newFiltration.value
            }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
