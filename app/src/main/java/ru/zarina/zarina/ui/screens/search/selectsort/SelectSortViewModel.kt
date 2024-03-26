package ru.zarina.zarina.ui.screens.search.selectsort

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.search.SearchViewModel

class SelectSortViewModel(
    private val searchSavedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<SelectSortViewModel.SideEffect> by SideEffectQueue() {

    val options = MutableStateFlow(
        persistentListOf(
            ProductSort.POPULARITY,
            ProductSort.PRICE,
            ProductSort.PRICE_DESCENDING,
        )
    ).asStateFlow()

    val selectedOption = searchSavedStateHandle
        .getStateFlow(SearchViewModel.KEY_SELECTED_SORT, ProductSort.DEFAULT)

    fun onOptionClick(option: ProductSort) {
        searchSavedStateHandle[SearchViewModel.KEY_SELECTED_SORT] = option
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
