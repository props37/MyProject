package ru.livetyping.zarina.feature.webview.ui.impl.impl

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.domain.model.common.ZARINA_HOST
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature
import javax.inject.Inject

@HiltViewModel
internal class WebViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<WebViewSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<WebViewFeature.NavEntry>()

    val url: StateFlow<String> = ReadOnlyStateFlow(navEntry.url)

    val headers: StateFlow<Map<String, String>> = ReadOnlyStateFlow(getHeaders(navEntry.url))

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = WebViewScreenAction.BackClicked
            emitSideEffect(WebViewSideEffect.Navigate(action))
        }
    }

    private fun getHeaders(url: String): Map<String, String> {
        val uri = url.toUri()
        return if (uri.host?.contains(ZARINA_HOST) == true) {
            mapOf(X_CLIENT_SOURCE_KEY to X_CLIENT_SOURCE_VALUE)
        } else {
            emptyMap()
        }
    }

    private companion object {
        private const val X_CLIENT_SOURCE_KEY = "x-client-source"
        private const val X_CLIENT_SOURCE_VALUE = "frontend"
    }
}
