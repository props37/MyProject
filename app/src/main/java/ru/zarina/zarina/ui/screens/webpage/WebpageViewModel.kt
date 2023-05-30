package ru.zarina.zarina.ui.screens.webpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class WebpageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    interactor: WebpageInteractor,
) : ViewModel(),
    ISideEffectSource<WebpageViewModel.SideEffect> by SideEffectQueue() {

    val headers = MutableStateFlow(interactor.getUserAgentHeaders())
        .mapState(viewModelScope) { it.toPersistentMap() }
    val url = savedStateHandle.getStateFlow(Destinations.Webpage.ARGUMENT_URL, "")

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
