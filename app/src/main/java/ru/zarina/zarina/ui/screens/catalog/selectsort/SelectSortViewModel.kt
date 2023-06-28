package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SelectSortViewModel(
    private val interactor: SelectSortInteractor,
) : ViewModel(),
    ISideEffectSource<SelectSortViewModel.SideEffect> by SideEffectQueue() {

    val options = MutableStateFlow(ProductSort.values().toList().toPersistentList())
        .asStateFlow()

    val selectedOption = interactor.sort.asStateFlow()

    fun onOptionClick(option: ProductSort) {
        interactor.sort.value = option
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
