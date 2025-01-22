package ru.livetyping.zarina.presentation.screen.webview

sealed interface WebViewScreenAction {
    data object BackClicked : WebViewScreenAction
}
