package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SelectSortViewModel @Inject constructor(
    private val interactor: SelectSortInteractor,
) : ViewModel(),
    ISideEffectSource<SelectSortViewModel.SideEffect> by SideEffectQueue() {

    val options = MutableStateFlow(ProductSort.values().toList().toPersistentList())
        .asStateFlow()

    fun onOptionClick(option: ProductSort) {
        // TODO
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
