package ru.livetyping.zarina.feature.webview.ui.impl.impl

internal sealed interface WebViewScreenAction {
    data object BackClicked : WebViewScreenAction
}
