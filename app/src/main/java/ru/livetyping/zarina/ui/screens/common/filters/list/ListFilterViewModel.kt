package ru.livetyping.zarina.ui.screens.common.filters.list

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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.ListFilter
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.base.ISideEffectSource
import ru.livetyping.zarina.ui.common.base.SideEffectQueue
import ru.livetyping.zarina.ui.navigation.old.destinations.Catalog
import ru.livetyping.zarina.ui.screens.catalog.filters.FilterType
import ru.livetyping.zarina.ui.screens.catalog.filters.FiltersViewModel
import ru.livetyping.zarina.utils.coroutine.mapState

@KoinViewModel
class ListFilterViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val parentSavedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<ListFilterViewModel.SideEffect> by SideEffectQueue() {

    private val filterType = savedStateHandle
        .getStateFlow<FilterType?>(Catalog.ListFilter.ARGUMENT_FILTER_TYPE, null)

    private val newFiltration =
        parentSavedStateHandle.getStateFlow<Filtration?>(
            key = FiltersViewModel.KEY_NEW_FILTRATION,
            initialValue = null
        )

    private val filterData = MutableStateFlow<ListFilter?>(null)

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

    fun onItemClick(item: ListFilter.Item) {
        filterData.update { filter ->
            if (filter?.isSingleSelection == true) {
                filter.copy(items = filter.items.map {
                    when {
                        it == item -> it.copy(isSelected = !it.isSelected)
                        it != item && it.isSelected -> it.copy(isSelected = false)
                        else -> it
                    }
                })
            } else {
                filter?.copy(items = filter.items.map { if (it == item) item.copy(isSelected = !item.isSelected) else it })
            }
        }
    }

    fun onClearClick() {
        filterData.update { filter ->
            filter?.copy(items = filter.items.map { it.copy(isSelected = false) })
        }
    }

    fun onApplyClick() {
        parentSavedStateHandle[FiltersViewModel.KEY_NEW_FILTRATION] =
            when (filterType.value) {
                FilterType.COLOR -> newFiltration.value?.copy(colors = filterData.value)
                FilterType.ATTRIBUTES -> newFiltration.value?.copy(attributes = filterData.value)
                FilterType.MATERIALS -> newFiltration.value?.copy(materials = filterData.value)
                FilterType.SIZE -> newFiltration.value?.copy(sizes = filterData.value)
                FilterType.CATEGORY -> newFiltration.value
                FilterType.PICKUP_SHOP -> newFiltration.value
                null -> newFiltration.value
            }
        sideEffect(SideEffect.GoBack)
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    private fun Filtration.getFilter(filterType: FilterType?): ListFilter? {
        return when (filterType) {
            FilterType.COLOR -> colors
            FilterType.ATTRIBUTES -> attributes
            FilterType.MATERIALS -> materials
            FilterType.SIZE -> sizes
            FilterType.CATEGORY -> null
            FilterType.PICKUP_SHOP -> null
            null -> null
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
