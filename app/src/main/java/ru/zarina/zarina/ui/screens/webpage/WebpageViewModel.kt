package ru.zarina.zarina.ui.screens.webpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class WebpageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: WebpageInteractor,
) : ViewModel(),
    ISideEffectSource<WebpageViewModel.SideEffect> by SideEffectQueue() {

    private val _headers = MutableStateFlow(interactor.getUserAgentHeaders())
    val headers = _headers
        .mapState(viewModelScope) { it.toPersistentMap() }
    val url = savedStateHandle.getStateFlow(Destinations.Webpage.ARGUMENT_URL, "")

    init {
        setupAuthorizationToken()
    }

    private fun setupAuthorizationToken() {
        viewModelScope.launch {
            interactor.getAuthorizationToken()
                .onSuccess { token ->
                    _headers.update { it + ("Authorization" to "Bearer ${token.token}") }
                }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
