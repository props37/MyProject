package ru.zarina.zarina.ui.screens.common.filters.tree

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.TreeFilter
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.filters.FilterType
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersViewModel
import ru.zarina.zarina.utils.coroutine.mapState

@KoinViewModel
class TreeFilterViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val parentSavedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<TreeFilterViewModel.SideEffect> by SideEffectQueue() {

    private val filterType = savedStateHandle
        .getStateFlow<FilterType?>(Catalog.ListFilter.ARGUMENT_FILTER_TYPE, null)

    private val newFiltration =
        parentSavedStateHandle.getStateFlow<Filtration?>(
            key = FiltersViewModel.KEY_NEW_FILTRATION,
            initialValue = null
        )

    private val filterData = MutableStateFlow<TreeFilter?>(null)

    val items = filterData
        .mapState(viewModelScope, SharingStarted.WhileSubscribed()) {
            it?.items.orEmpty().toPersistentList()
        }

    val toolbarTitle = filterType.map {
        when (it) {
            FilterType.CATEGORY -> Text.Resource(R.string.categories)
            FilterType.COLOR -> Text.Resource(R.string.color)
            FilterType.ATTRIBUTES -> Text.Resource(R.string.attributes)
            FilterType.MATERIALS -> Text.Resource(R.string.materials)
            FilterType.SIZE -> Text.Resource(R.string.size)
            FilterType.PICKUP_SHOP -> Text.Resource(R.string.selection_of_store)
            null -> Text.Empty
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Text.Empty)

    val isApplyButtonVisible = combine(newFiltration, filterData) { filtration, filterData ->
        val filtrationData = filtration?.getFilter(filterType.value)
        filtrationData != filterData
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isClearButtonVisible = filterData.map { it?.isEmpty == false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        newFiltration
            .onEach { filtration ->
                filterData.value = filtration?.getFilter(filterType.value)
            }
            .launchIn(viewModelScope)
    }

    fun onItemClick(item: TreeFilter.Item) {
        filterData.update { filter ->
            if (filter?.isSingleSelection == true) {
                filter.copy(items = filter.items.map {
                    when {
                        it == item -> it.copy(isExplicitSelected = !it.isSelected)
                        it != item && it.isSelected -> it.copy(isExplicitSelected = false)
                        else -> it
                    }
                })
            } else {
                filter?.copy(items = filter.items.map { if (it == item) item.copy(isExplicitSelected = !item.isSelected) else it })
            }
        }
    }

    fun onClearClick() {
        filterData.update { filter ->
            filter?.copy(items = filter.items.map { it.copy(isExplicitSelected = false) })
        }
    }

    fun onApplyClick() {
        parentSavedStateHandle[FiltersViewModel.KEY_NEW_FILTRATION] =
            when (filterType.value) {
                FilterType.CATEGORY -> newFiltration.value?.copy(categories = filterData.value)
                else -> newFiltration.value
            }
        sideEffect(SideEffect.GoBack)
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    private fun Filtration.getFilter(filterType: FilterType?): TreeFilter? {
        return when (filterType) {
            FilterType.CATEGORY -> categories
            else -> null
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
