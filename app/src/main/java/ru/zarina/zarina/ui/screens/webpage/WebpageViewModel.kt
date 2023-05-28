package ru.zarina.zarina.ui.screens.webpage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class WebpageViewModel @Inject constructor(
    private val interactor: WebpageInteractor,
) : ViewModel(),
    ISideEffectSource<WebpageViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
