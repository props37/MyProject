package ru.zarina.zarina.ui.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: HomeInteractor,
) : ViewModel(),
    ISideEffectSource<HomeViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
