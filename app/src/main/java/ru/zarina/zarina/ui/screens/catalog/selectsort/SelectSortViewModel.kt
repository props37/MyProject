package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.catalog.products.ProductsViewModel

class SelectSortViewModel(
    private val productSavedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<SelectSortViewModel.SideEffect> by SideEffectQueue() {

    val options = MutableStateFlow(ProductSort.values().toList().toPersistentList())
        .asStateFlow()

    val selectedOption = productSavedStateHandle
        .getStateFlow(ProductsViewModel.KEY_SELECTED_SORT, ProductSort.DEFAULT)

    fun onOptionClick(option: ProductSort) {
        productSavedStateHandle[ProductsViewModel.KEY_SELECTED_SORT] = option
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
