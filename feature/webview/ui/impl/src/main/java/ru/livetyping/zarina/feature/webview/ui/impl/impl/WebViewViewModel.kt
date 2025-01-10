package ru.livetyping.zarina.feature.webview.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
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

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = WebViewScreenAction.BackClicked
            emitSideEffect(WebViewSideEffect.Navigate(action))
        }
    }
}
