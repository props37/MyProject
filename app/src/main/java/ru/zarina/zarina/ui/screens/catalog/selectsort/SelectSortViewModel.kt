package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SelectSortViewModel @Inject constructor(
    private val interactor: SelectSortInteractor,
) : ViewModel(),
    ISideEffectSource<SelectSortViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
