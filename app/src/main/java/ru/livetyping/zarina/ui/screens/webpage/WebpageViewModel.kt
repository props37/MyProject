package ru.livetyping.zarina.ui.screens.webpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.livetyping.zarina.domain.old.AuthorizationToken
import ru.livetyping.zarina.ui.common.base.ISideEffectSource
import ru.livetyping.zarina.ui.common.base.SideEffectQueue
import ru.livetyping.zarina.ui.navigation.old.destinations.Destinations
import ru.livetyping.zarina.utils.coroutine.mapState

@KoinViewModel
class WebpageViewModel(
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

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    private fun loadAuthorizationToken() {
        viewModelScope.launch {
            authorizationToken.value = interactor.getAuthorizationToken()
        }
    }

    enum class ErrorType { GENERIC }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
