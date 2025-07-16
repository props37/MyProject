package ru.livetyping.zarina.feature.webview.ui.impl.screen

internal sealed interface WebViewScreenAction {
    data object BackClicked : WebViewScreenAction
}
