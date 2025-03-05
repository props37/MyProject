package ru.livetyping.zarina.presentation.screen.webview

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.webview.WebViewViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<UnscopedDestinations.WebView>()

    val url: StateFlow<String> = ImmutableStateFlow(navEntry.url)

    val headers: StateFlow<Map<String, String>> = ImmutableStateFlow(getHeaders(navEntry.url))

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = WebViewScreenAction.BackClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun getHeaders(url: String): Map<String, String> {
        val uri = url.toUri()
        return if (uri.host == ZARINA_HOST) {
            mapOf(HEADER_X_CLIENT_SOURCE_KEY to HEADER_X_CLIENT_SOURCE_VALUE)
        } else {
            emptyMap()
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: WebViewScreenAction) : SideEffect
    }

    private companion object {
        private const val ZARINA_HOST = "zarina.ru"
        private const val HEADER_X_CLIENT_SOURCE_KEY = "x-client-source"
        private const val HEADER_X_CLIENT_SOURCE_VALUE = "frontend"
    }
}