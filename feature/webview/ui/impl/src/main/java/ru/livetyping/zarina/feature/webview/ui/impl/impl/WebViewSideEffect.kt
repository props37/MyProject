package ru.livetyping.zarina.feature.webview.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface WebViewSideEffect : SideEffect {
    data class Navigate(val action: WebViewScreenAction) : WebViewSideEffect
}
