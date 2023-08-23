package ru.zarina.zarina.ui.screens.search.filters

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class FiltersViewModel(
    private val interactor: FiltersInteractor,
) : ViewModel(),
    ISideEffectSource<FiltersViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
