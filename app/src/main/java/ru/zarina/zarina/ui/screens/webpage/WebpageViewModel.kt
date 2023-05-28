package ru.zarina.zarina.ui.screens.webpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import javax.inject.Inject

@HiltViewModel
class WebpageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: WebpageInteractor,
) : ViewModel(),
    ISideEffectSource<WebpageViewModel.SideEffect> by SideEffectQueue() {

    val url = savedStateHandle.getStateFlow(Destinations.Webpage.ARGUMENT_URL, "")

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
