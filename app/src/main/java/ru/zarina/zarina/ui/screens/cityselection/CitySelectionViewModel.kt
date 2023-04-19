package ru.zarina.zarina.ui.screens.cityselection

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class CitySelectionViewModel @Inject constructor(
    private val interactor: CitySelectionInteractor,
) : ViewModel(),
    ISideEffectSource<CitySelectionViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
