package ru.zarina.zarina.ui.screens.catalog.categories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val interactor: CategoriesInteractor,
) : ViewModel(),
    ISideEffectSource<CategoriesViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
