package ru.zarina.zarina.ui.screens.webpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.AuthorizationToken
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

    private val authorizationToken = MutableStateFlow<Result<AuthorizationToken>?>(null)
    val headers = authorizationToken.mapState(viewModelScope) { result ->
        val token = result?.getOrNull() ?: return@mapState null
        (interactor.getUserAgentHeaders() + ("Authorization" to "Bearer ${token.token}")).toPersistentMap()
    }
    val errorType = authorizationToken.mapState(viewModelScope) { result ->
        when {
            result?.isFailure == true -> ErrorType.GENERIC
            else -> null
        }
    }
    val url = savedStateHandle.getStateFlow(Destinations.Webpage.ARGUMENT_URL, "")

    init {
        loadAuthorizationToken()
    }

    fun onReloadClick() = loadAuthorizationToken()

    private fun loadAuthorizationToken() {
        viewModelScope.launch {
            authorizationToken.value = interactor.getAuthorizationToken()
        }
    }

    enum class ErrorType { GENERIC }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
